package com.hostel.repository;

import com.hostel.entity.Wallet;
import com.hostel.entity.WalletTransaction;
import com.hostel.enums.WalletTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access repository for {@link WalletTransaction}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>This is the authoritative, append-only ledger backing
 * {@link Wallet#getCurrentBalance()}.</p>
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
}
