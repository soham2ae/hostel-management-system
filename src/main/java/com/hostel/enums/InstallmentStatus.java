package com.hostel.enums;

/**
 * Represents the stored state of an installment.
 *
 * <p><b>Important:</b> {@code OVERDUE} is intentionally not a stored
 * value of this enum. Whether an installment is overdue is a derived
 * fact, computed dynamically as:</p>
 *
 * <pre>
 *     status == InstallmentStatus.PENDING &amp;&amp; dueDate.isBefore(currentDate)
 * </pre>
 *
 * <p>This computation belongs in the service layer and must never be
 * persisted as a distinct status value.</p>
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code installment} entity to ensure readability in the database
 * and to avoid ordinal-related data corruption if values are
 * reordered in the future.</p>
 */
public enum InstallmentStatus {

    /** Installment has not yet been paid. */
    PENDING,

    /** Installment has been paid in full. */
    PAID;

    /**
     * Returns a human-readable display label for this installment status.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case PENDING:
                return "Pending";
            case PAID:
                return "Paid";
            default:
                throw new IllegalStateException("Unhandled InstallmentStatus: " + this);
        }
    }
}
