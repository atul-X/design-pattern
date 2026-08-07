package lld.ticketmaster.pattern.strategy;

import lld.ticketmaster.enums.PaymentMethod;
import lld.ticketmaster.model.Payment;

/**
 * Strategy pattern for payment processing.
 *
 * Each strategy encapsulates one payment gateway's integration logic.
 * PaymentService stays stable; adding a new method (e.g. Apple Pay)
 * means adding one new class — no changes to existing code (OCP).
 *
 * execute() marks payment.markSuccess() on success,
 * or throws PaymentException on failure — never returns a partially mutated payment.
 */
public interface PaymentStrategy {
    void execute(Payment payment);
    PaymentMethod getMethod();
}