package com.hostel.exception;

/**
 * Thrown when an attempt is made to create a hostel using a hostel
 * code or hostel name that already exists.
 *
 * <p>Both {@code hostel_code} and {@code hostel_name} are unique
 * constraints on the {@code hostel} table. This exception is thrown
 * proactively by the service layer before persistence is attempted,
 * rather than allowing a raw database constraint violation to surface.
 * Maps to {@code 409 CONFLICT} in {@code GlobalExceptionHandler},
 * inherited from {@link BusinessRuleViolationException}.</p>
 */
public class DuplicateHostelException extends BusinessRuleViolationException {

    /**
     * Constructs a new {@code DuplicateHostelException} describing
     * which field collided and what value was supplied.
     *
     * @param fieldName  the name of the field that collided
     *                   (e.g. "hostel code", "hostel name")
     * @param fieldValue the value that already exists
     */
    public DuplicateHostelException(String fieldName, String fieldValue) {
        super("A hostel already exists with " + fieldName + ": '" + fieldValue + "'");
    }
}
