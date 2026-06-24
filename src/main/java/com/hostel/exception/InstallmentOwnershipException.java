package com.hostel.exception;

/**
 * Thrown when an attempt is made to settle an installment that does
 * not belong to the student making the payment.
 *
 * <p>Treated as a business-rule conflict rather than an authorization
 * failure: the installment and the student both exist and the request
 * is otherwise well-formed, but the data relationship between them
 * does not hold. The installment's status is left unchanged when this
 * exception is thrown. Maps to {@code 409 CONFLICT} in
 * {@code GlobalExceptionHandler}, inherited from
 * {@link BusinessRuleViolationException}.</p>
 */
public class InstallmentOwnershipException extends BusinessRuleViolationException {

    /**
     * Constructs a new {@code InstallmentOwnershipException} for the
     * given installment and student identifiers.
     *
     * @param installmentId the identifier of the installment that was
     *                      attempted to be settled
     * @param studentId     the identifier of the student who does not
     *                      own the installment
     */
    public InstallmentOwnershipException(Long installmentId, Long studentId) {
        super("Installment id " + installmentId + " does not belong to student id " + studentId);
    }
}
