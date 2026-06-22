package com.hostel.enums;

/**
 * Represents the type of food service generating revenue.
 *
 * <p><b>Important:</b> {@code FoodType} and {@link PaymentType} are
 * intentionally separate enums. {@code FoodType} describes the food
 * service itself, whereas {@code PaymentType} describes why money was
 * paid. The two must not be merged or used interchangeably.</p>
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code food_transaction} entity to ensure readability in the
 * database and to avoid ordinal-related data corruption if values
 * are reordered in the future.</p>
 */
public enum FoodType {

    /** Regular lunch service. */
    LUNCH,

    /** À la carte food service. */
    ALACARTE;

    /**
     * Returns a human-readable display label for this food type.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case LUNCH:
                return "Lunch";
            case ALACARTE:
                return "A La Carte";
            default:
                throw new IllegalStateException("Unhandled FoodType: " + this);
        }
    }
}
