package com.hostel.repository;

import com.hostel.entity.Wallet;
import com.hostel.entity.WalletTransaction;
import com.hostel.enums.WalletTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data access repository for {@link WalletTransaction}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>This is the authoritative, append-only ledger backing
 * {@link Wallet#getCurrentBalance()}.</p>
 *
 * <p>Aggregation methods on this repository are read-side projections
 * used for dashboard wallet statistics. They do not persist derived
 * values — nothing computed here is written back to the database.</p>
 */
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    /**
     * Finds all wallet transactions belonging to a given wallet.
     *
     * @param wallet the wallet
     * @return wallet transactions for the wallet
     */
    List<WalletTransaction> findByWallet(Wallet wallet);

    /**
     * Finds all wallet transactions with the given transaction type.
     *
     * @param transactionType the transaction type
     * @return wallet transactions with the given type
     */
    List<WalletTransaction> findByTransactionType(WalletTransactionType transactionType);

    /**
     * Finds all wallet transactions for a given wallet and transaction
     * type.
     *
     * @param wallet          the wallet
     * @param transactionType the transaction type
     * @return wallet transactions matching both criteria
     */
    List<WalletTransaction> findByWalletAndTransactionType(Wallet wallet, WalletTransactionType transactionType);

    /**
     * Finds all top-up transactions for a given wallet.
     *
     * @param wallet the wallet
     * @return top-up transactions for the wallet
     */
    default List<WalletTransaction> findTopUpsByWallet(Wallet wallet) {
        return findByWalletAndTransactionType(wallet, WalletTransactionType.TOPUP);
    }

    /**
     * Finds all debit transactions for a given wallet.
     *
     * @param wallet the wallet
     * @return debit transactions for the wallet
     */
    default List<WalletTransaction> findDebitsByWallet(Wallet wallet) {
        return findByWalletAndTransactionType(wallet, WalletTransactionType.DEBIT);
    }

    /**
     * Calculates the total top-up amount credited across all wallets.
     *
     * <p>Read-side projection used for dashboard wallet statistics; not
     * a stored value. Sums {@code creditAmount} over all
     * {@code TOPUP}-type transactions.</p>
     *
     * @return the total top-up amount, or {@code null} if there are no
     *         top-up transactions
     */
    @Query("SELECT SUM(wt.creditAmount) FROM WalletTransaction wt WHERE wt.transactionType = 'TOPUP'")
    BigDecimal sumTotalTopupAmount();

    /**
     * Calculates the total debit amount consumed across all wallets.
     *
     * <p>Read-side projection used for dashboard wallet statistics; not
     * a stored value. Sums {@code transactionAmount} over all
     * {@code DEBIT}-type transactions.</p>
     *
     * @return the total debit amount, or {@code null} if there are no
     *         debit transactions
     */
    @Query("SELECT SUM(wt.transactionAmount) FROM WalletTransaction wt WHERE wt.transactionType = 'DEBIT'")
    BigDecimal sumTotalDebitAmount();
}
