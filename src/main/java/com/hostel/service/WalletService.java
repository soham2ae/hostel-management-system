package com.hostel.service;

import com.hostel.entity.Wallet;
import com.hostel.entity.WalletTransaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service contract for managing {@link Wallet} records and their
 * associated {@link WalletTransaction} ledger.
 *
 * <p>{@link Wallet#getCurrentBalance()} is a cached value maintained
 * exclusively by this service; the {@link WalletTransaction} ledger is
 * the authoritative source of truth. Every balance-affecting operation
 * here creates a corresponding ledger entry in the same transaction
 * that updates the cached balance.</p>
 *
 * <p>Wallet operations are entirely separate from
 * {@link com.hostel.entity.Payment} records. This service has no
 * dependency on payment-related repositories or services, and never
 * creates a {@code Payment} as a side effect of a wallet operation.</p>
 *
 * <p>Negative balances are forbidden; a debit that would take the
 * balance below zero is rejected outright rather than clamped or
 * silently adjusted. A balance of exactly zero is permitted.</p>
 */
public interface WalletService {

    /**
     * Creates a new wallet for a student, with an initial balance of
     * {@link BigDecimal#ZERO}.
     *
     * <p>Resolves the owning {@link com.hostel.entity.Student} from
     * {@code studentId} and attaches it to the new wallet before
     * persisting. A wallet has no other meaningful fields for a caller
     * to supply at creation time, so no entity parameter is accepted.</p>
     *
     * @param studentId the identifier of the student this wallet
     *                  belongs to
     * @return the created wallet, with its generated identifier populated
     */
    Wallet createWallet(Long studentId);

    /**
     * Finds a wallet by its identifier.
     *
     * @param walletId the wallet identifier
     * @return the matching wallet
     */
    Wallet findWalletById(Long walletId);

    /**
     * Finds the wallet belonging to a given student.
     *
     * @param studentId the student identifier
     * @return the matching wallet
     */
    Wallet findWalletByStudent(Long studentId);

    /**
     * Tops up a student's wallet.
     *
     * <p>Increases {@link Wallet#getCurrentBalance()} by
     * {@code creditAmount} — not {@code actualAmountPaid} — since a
     * top-up may include a promotional credit greater than the amount
     * physically paid (e.g. paying 10,000 to receive a credit of
     * 15,000). Records a {@link WalletTransaction} with
     * {@code transactionType = TOPUP}, both amounts populated, and
     * {@code transactionAmount} left {@code null}.</p>
     *
     * <p>Both {@code actualAmountPaid} and {@code creditAmount} must be
     * strictly positive; zero or negative values are rejected.</p>
     *
     * @param studentId        the identifier of the student topping up
     * @param actualAmountPaid the amount physically paid by the student
     * @param creditAmount     the amount credited to the wallet
     * @return the updated wallet
     */
    Wallet topUp(Long studentId, BigDecimal actualAmountPaid, BigDecimal creditAmount);

    /**
     * Debits a student's wallet.
     *
     * <p>Decreases {@link Wallet#getCurrentBalance()} by
     * {@code transactionAmount}. Records a {@link WalletTransaction}
     * with {@code transactionType = DEBIT}, {@code transactionAmount}
     * populated, and both {@code actualAmountPaid} and
     * {@code creditAmount} left {@code null}.</p>
     *
     * <p>{@code transactionAmount} must be strictly positive; zero or
     * negative values are rejected. The debit itself is rejected if it
     * would take the balance below zero — a resulting balance of
     * exactly zero is permitted.</p>
     *
     * @param studentId         the identifier of the student debiting
     *                          their wallet
     * @param transactionAmount the amount to consume from the wallet
     * @return the updated wallet
     */
    Wallet debit(Long studentId, BigDecimal transactionAmount);

    /**
     * Finds all wallet transactions for a given student's wallet.
     *
     * @param studentId the student identifier
     * @return all wallet transactions for the student's wallet
     */
    List<WalletTransaction> findWalletTransactions(Long studentId);
}
