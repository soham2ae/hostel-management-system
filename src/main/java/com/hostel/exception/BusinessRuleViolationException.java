package com.hostel.exception;

/**
 * Thrown when a well-formed request conflicts with the current state
 * of a resource, violating a business rule.
 *
 * <p>This is the general parent for "state conflict" failures — the
 * request itself is valid and the resource exists, but the operation
 * cannot proceed given the resource's current state (e.g. a bed is
 * already occupied, a wallet balance is too low, a booking number is
 * already in use). Distinct from {@link ValidationException}, where
 * the input itself is malformed. Maps to {@code 409 CONFLICT} in
 * {@code GlobalExceptionHandler}.</p>
 *
 * <p>Concrete subtypes: {@link DuplicateBookingException},
 * {@link DuplicateHostelException}, {@link DuplicateUsernameException},
 * {@link BedOccupiedException},
 * {@link InsufficientWalletBalanceException}, and
 * {@link InstallmentOwnershipException}.</p>
 */
public abstract class BusinessRuleViolationException extends HostelException {

    /**
     * Constructs a new {@code BusinessRuleViolationException} with the
     * given message.
     *
     * @param message the detail message
     */
    protected BusinessRuleViolationException(String message) {
        super(message);
    }
}
