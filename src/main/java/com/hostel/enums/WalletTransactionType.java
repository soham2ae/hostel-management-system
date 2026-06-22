package com.hostel.enums;

/**
 * Represents the type of operation performed on a student's wallet.
 *
 * <p>For {@code TOPUP} transactions, {@code actual_amount_paid} and
 * {@code credit_amount} are populated and {@code transaction_amount}
 * is {@code NULL}. For {@code DEBIT} transactions, {@code transaction_amount}
 * is populated and {@code actual_amount_paid} / {@code credit_amount}
 * are {@code NULL}.</p>
 *
 * <p>Stored using {@code @Enumerated(EnumType.STRING)} on the
 * {@code wallet_transaction} entity to ensure readability in the
 * database and to avoid ordinal-related data corruption if values
 * are reordered in the future.</p>
 */
public enum WalletTransactionType {

    /** Student pays money and the wallet is credited (potentially with a bonus). */
    TOPUP,

    /** Student consumes balance from the wallet. */
    DEBIT;

    /**
     * Returns a human-readable display label for this wallet transaction type.
     *
     * @return the display label
     */
    public String getDisplayLabel() {
        switch (this) {
            case TOPUP:
                return "Top Up";
            case DEBIT:
                return "Debit";
            default:
                throw new IllegalStateException("Unhandled WalletTransactionType: " + this);
        }
    }
}
