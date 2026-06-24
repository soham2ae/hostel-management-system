package com.hostel.exception;

/**
 * Thrown when an attempt is made to create a student booking using a
 * booking number that already exists.
 *
 * <p>Booking numbers are globally unique across all hostels and years.
 * This exception is thrown proactively by the service layer before
 * persistence is attempted, rather than allowing a raw database
 * constraint violation to surface. Maps to {@code 409 CONFLICT} in
 * {@code GlobalExceptionHandler}, inherited from
 * {@link BusinessRuleViolationException}.</p>
 */
public class DuplicateBookingException extends BusinessRuleViolationException {

    /**
     * Constructs a new {@code DuplicateBookingException} for the given
     * booking number.
     *
     * @param bookingNo the booking number that already exists
     */
    public DuplicateBookingException(String bookingNo) {
        super("A student booking already exists with booking no: '" + bookingNo + "'");
    }
}
