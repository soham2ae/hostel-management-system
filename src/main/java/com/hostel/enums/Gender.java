package com.hostel.enums;

/**
 * Represents the gender of a student.
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code student} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 */
public enum Gender {

    /** Male student. */
    MALE,

    /** Female student. */
    FEMALE;

    /**
     * Returns a human-readable display label for this gender.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case MALE:
                return "Male";
            case FEMALE:
                return "Female";
            default:
                throw new IllegalStateException("Unhandled Gender: " + this);
        }
    }
}
