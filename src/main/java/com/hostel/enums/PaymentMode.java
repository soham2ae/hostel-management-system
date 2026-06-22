package com.hostel.enums;

/**
 * Represents the method by which a payment was made.
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code payment} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 */
public enum PaymentMode {

    /** Payment made in cash. */
    CASH,

    /** Payment made via UPI. */
    UPI,

    /** Payment made via debit/credit card. */
    CARD,

    /** Payment made via bank transfer. */
    BANK_TRANSFER,

    /** Payment made via cheque. */
    CHEQUE;

    /**
     * Returns a human-readable display label for this payment mode.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case CASH:
                return "Cash";
            case UPI:
                return "UPI";
            case CARD:
                return "Card";
            case BANK_TRANSFER:
                return "Bank Transfer";
            case CHEQUE:
                return "Cheque";
            default:
                throw new IllegalStateException("Unhandled PaymentMode: " + this);
        }
    }
}
