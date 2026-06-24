package com.hostel.exception;

/**
 * Thrown when an attempt is made to assign a student to a bed that is
 * already occupied.
 *
 * <p>An occupied bed is never silently reassigned or swapped to a new
 * occupant — data integrity takes priority over convenience. Maps to
 * {@code 409 CONFLICT} in {@code GlobalExceptionHandler}, inherited
 * from {@link BusinessRuleViolationException}.</p>
 */
public class BedOccupiedException extends BusinessRuleViolationException {

    /**
     * Constructs a new {@code BedOccupiedException} for the given bed
     * identifier.
     *
     * @param bedId the identifier of the bed that is already occupied
     */
    public BedOccupiedException(Long bedId) {
        super("Bed is already occupied and cannot be assigned: " + bedId);
    }
}
