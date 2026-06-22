package com.hostel.enums;

/**
 * Defines the sharing type / typology of a room.
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code room} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 *
 * <p>Enum constant names follow standard Java naming conventions
 * and therefore differ from their human-readable display labels
 * (e.g. {@code DOUBLE_2_PLUS_1} displays as {@code "Double(2+1)"}).
 * Use {@link #getDisplayLabel()} when rendering this value to users.</p>
 */
public enum RoomTypology {

    /** Single occupancy room. */
    SINGLE,

    /** Twin sharing room (2 beds). */
    TWIN,

    /** Triple sharing room (3 beds). */
    TRIPLE,

    /** Quad sharing room (4 beds). */
    QUAD,

    /** Double room with an additional 1 bed (2+1 configuration). */
    DOUBLE_2_PLUS_1,

    /** Double room with an additional 2 beds (2+2 configuration). */
    DOUBLE_2_PLUS_2,

    /** Single room with an additional 1 bed (2+1 configuration). */
    SINGLE_2_PLUS_1;

    /**
     * Returns the human-readable display label for this room typology,
     * matching the business-facing naming convention (e.g. {@code "Double(2+1)"}).
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case SINGLE:
                return "Single";
            case TWIN:
                return "Twin";
            case TRIPLE:
                return "Triple";
            case QUAD:
                return "Quad";
            case DOUBLE_2_PLUS_1:
                return "Double(2+1)";
            case DOUBLE_2_PLUS_2:
                return "Double(2+2)";
            case SINGLE_2_PLUS_1:
                return "Single(2+1)";
            default:
                throw new IllegalStateException("Unhandled RoomTypology: " + this);
        }
    }
}
