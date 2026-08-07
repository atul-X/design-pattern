package lld.ticketmaster.service;

import lld.ticketmaster.enums.PaymentStatus;
import lld.ticketmaster.exception.PaymentException;
import lld.ticketmaster.model.Payment;
import lld.ticketmaster.pattern.strategy.PaymentStrategy;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Idempotent payment orchestrator.
 *
 * The bookingId is the idempotency key — retrying with the same bookingId
 * returns the original payment result rather than charging twice.
 * (PayPay's double-charge is a P0 incident; this is non-negotiable.)
 *
 * Payment gateway details (which API, what headers, retry policy) are
 * encapsulated in the injected PaymentStrategy. Swapping from PayPay Wallet
 * to Credit Card requires no change here.
 */
public class PaymentService {

    private final Map<String, Payment> payments         = new ConcurrentHashMap<>();
    private final Map<String, String>  idempotencyStore = new ConcurrentHashMap<>(); // bookingId → paymentId

    public Payment processPayment(String bookingId, String userId,
                                  double amount, PaymentStrategy strategy) {
        // Idempotency: same bookingId always returns the same result
        String existingId = idempotencyStore.get(bookingId);
        if (existingId != null) {
            Payment existing = payments.get(existingId);
            if (existing.getStatus() == PaymentStatus.SUCCESS) return existing;
            throw new PaymentException("Previous payment attempt failed for booking: " + bookingId);
        }

        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, bookingId, userId, amount, strategy.getMethod());
        payments.put(paymentId, payment);

        // Register before calling gateway: crash-safe — a retry finds this record
        // and won't create a duplicate charge.
        idempotencyStore.put(bookingId, paymentId);

        try {
            strategy.execute(payment); // strategy marks success or throws
            return payment;
        } catch (PaymentException e) {
            payment.markFailed();
            throw e;
        } catch (Exception e) {
            payment.markFailed();
            throw new PaymentException("Payment gateway error: " + e.getMessage(), e);
        }
    }

    public Payment refundPayment(String paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment == null)
            throw new IllegalArgumentException("Payment not found: " + paymentId);
        if (payment.getStatus() != PaymentStatus.SUCCESS)
            throw new IllegalStateException("Can only refund a successful payment, current: " + payment.getStatus());
        payment.markRefunded();
        return payment;
    }

    public Payment getPayment(String paymentId) {
        return payments.get(paymentId);
    }
}