package com.hostel.enums;

/**
 * Represents the reason a payment was made.
 *
 * <p><b>Important:</b> {@code PaymentType} must not be confused with
 * {@link FoodType}. {@code PaymentType} describes <i>why</i> money was
 * paid (the business reason behind a transaction), whereas
 * {@code FoodType} describes <i>which food service</i> generated a
 * charge. The two enums are intentionally separate and must not be
 * merged or used interchangeably.</p>
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code payment} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 */
public enum PaymentType {

    /** Advance payment made at the time of booking. */
    BOOKING,

    /** Payment collected at the time of move-in. */
    MOVE_IN,

    /** Payment made against an installment. */
    INSTALLMENT,

    /** Payment related to food charges. */
    FOOD,

    /** Wallet or à la carte related payment. */
    A_LA_CARTE;

    /**
     * Returns a human-readable display label for this payment type.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case BOOKING:
                return "Booking";
            case MOVE_IN:
                return "Move In";
            case INSTALLMENT:
                return "Installment";
            case FOOD:
                return "Food";
            case A_LA_CARTE:
                return "A La Carte";
            default:
                throw new IllegalStateException("Unhandled PaymentType: " + this);
        }
    }
}
