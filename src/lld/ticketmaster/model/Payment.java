package lld.ticketmaster.model;

import lld.ticketmaster.enums.PaymentMethod;
import lld.ticketmaster.enums.PaymentStatus;

import java.time.Instant;

public class Payment {
    private final String paymentId;
    private final String bookingId; // also serves as idempotency key
    private final String userId;
    private final double amount;
    private final PaymentMethod method;
    private volatile PaymentStatus status;
    private final Instant createdAt;

    public Payment(String paymentId, String bookingId, String userId,
                   double amount, PaymentMethod method) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public synchronized void markSuccess()  { this.status = PaymentStatus.SUCCESS; }
    public synchronized void markFailed()   { this.status = PaymentStatus.FAILED; }
    public synchronized void markRefunded() { this.status = PaymentStatus.REFUNDED; }

    public String getPaymentId()      { return paymentId; }
    public String getBookingId()      { return bookingId; }
    public String getUserId()         { return userId; }
    public double getAmount()         { return amount; }
    public PaymentMethod getMethod()  { return method; }
    public PaymentStatus getStatus()  { return status; }
    public Instant getCreatedAt()     { return createdAt; }

    @Override
    public String toString() {
        return String.format("Payment[%s | booking=%s | ¥%.0f | %s | %s]",
                paymentId, bookingId, amount, method, status);
    }
}