package com.hostel.enums;

/**
 * Represents the current lifecycle state of a student's booking/stay.
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code student} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 */
public enum StudentStatus {

    /** Booking has been confirmed but the student has not yet moved in. */
    CONFIRMED,

    /** Student is currently residing in the hostel. */
    ACTIVE,

    /** Student joined during the middle of the season (typically December). */
    MID_SEASON,

    /** Student has completed their stay and left the hostel. */
    CHECKED_OUT,

    /** Booking was cancelled. */
    CANCELLED;

    /**
     * Returns a human-readable display label for this student status.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case CONFIRMED:
                return "Confirmed";
            case ACTIVE:
                return "Active";
            case MID_SEASON:
                return "Mid Season";
            case CHECKED_OUT:
                return "Checked Out";
            case CANCELLED:
                return "Cancelled";
            default:
                throw new IllegalStateException("Unhandled StudentStatus: " + this);
        }
    }
}
