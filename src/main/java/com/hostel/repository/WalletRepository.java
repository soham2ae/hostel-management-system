package com.hostel.repository;

import com.hostel.entity.Student;
import com.hostel.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Data access repository for {@link Wallet}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>{@link Wallet#getCurrentBalance()} is a cached value; the
 * authoritative source of truth for wallet balance is the
 * {@link com.hostel.entity.WalletTransaction} ledger, accessed via
 * {@link WalletTransactionRepository}.</p>
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Finds the wallet belonging to a given student.
     *
     * @param student the student
     * @return the matching wallet, if any
     */
    Optional<Wallet> findByStudent(Student student);

    /**
     * Calculates the total current balance across all wallets.
     *
     * <p>Read-side projection used for dashboard wallet statistics; not
     * a stored value. Sums {@link Wallet#getCurrentBalance()}, the
     * cached balance, rather than recomputing from the
     * {@code WalletTransaction} ledger.</p>
     *
     * @return the total current balance, or {@code null} if there are
     *         no wallets
     */
    @Query("SELECT SUM(w.currentBalance) FROM Wallet w")
    BigDecimal sumTotalCurrentBalance();
}
