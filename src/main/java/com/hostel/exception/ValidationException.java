package com.hostel.exception;

/**
 * Thrown when input supplied to a service operation fails a validation
 * rule.
 *
 * <p>This is the general parent for input-validation failures — the
 * request itself is malformed or out of an acceptable range, as
 * opposed to a well-formed request conflicting with current resource
 * state (see {@link BusinessRuleViolationException}). Maps to
 * {@code 400 BAD_REQUEST} in {@code GlobalExceptionHandler}.</p>
 *
 * <p>{@link InvalidAmountException} is the current concrete subtype.
 * Future field-level validation failures (e.g. malformed contact
 * details, invalid date ranges) may extend this class directly without
 * requiring a new named subtype for every field.</p>
 */
public class ValidationException extends HostelException {

    /**
     * Constructs a new {@code ValidationException} with the given
     * message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }
}
