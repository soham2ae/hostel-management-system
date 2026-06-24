package com.hostel.exception;

import java.math.BigDecimal;

/**
 * Thrown when a wallet debit would take the current balance below
 * zero.
 *
 * <p>Negative wallet balances are forbidden. The debit is rejected
 * outright — never clamped or silently adjusted — leaving the wallet's
 * balance and transaction ledger completely unchanged. A resulting
 * balance of exactly zero is permitted and does not trigger this
 * exception. Maps to {@code 409 CONFLICT} in
 * {@code GlobalExceptionHandler}, inherited from
 * {@link BusinessRuleViolationException}.</p>
 */
public class InsufficientWalletBalanceException extends BusinessRuleViolationException {

    /**
     * Constructs a new {@code InsufficientWalletBalanceException}
     * describing the current balance and the amount that was rejected.
     *
     * @param currentBalance    the wallet's current balance
     * @param transactionAmount the debit amount that was rejected
     */
    public InsufficientWalletBalanceException(BigDecimal currentBalance, BigDecimal transactionAmount) {
        super("Insufficient wallet balance: current balance " + currentBalance
                + " is less than debit amount " + transactionAmount);
    }
}
