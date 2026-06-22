package com.hostel.enums;

/**
 * Represents the occupancy status of a bed.
 *
 * <p>This value reflects the bed's current state only and is the
 * source of truth from which room-level availability (e.g.
 * "2/3 Beds Available" or "BOOKED") must be derived dynamically.
 * Room-level availability must never be stored.</p>
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code bed} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 */
public enum BedStatus {

    /** Bed is available and not assigned to any student. */
    VACANT,

    /** Bed currently has a student assigned. */
    OCCUPIED;

    /**
     * Returns a human-readable display label for this bed status.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case VACANT:
                return "Vacant";
            case OCCUPIED:
                return "Occupied";
            default:
                throw new IllegalStateException("Unhandled BedStatus: " + this);
        }
    }
}
