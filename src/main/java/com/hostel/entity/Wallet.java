package com.hostel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the wallet balance for a single {@link Student} booking
 * year.
 *
 * <p>One {@link Student} has exactly one {@code Wallet} record per
 * booking year.</p>
 *
 * <p><b>Important:</b> {@link #currentBalance} is intentionally stored
 * as a cached value for read performance. The source of truth for the
 * wallet's balance is the append-only {@link WalletTransaction} ledger.
 * Every wallet operation must create a corresponding
 * {@code WalletTransaction} row and update {@link #currentBalance}
 * transactionally in the same operation — {@link #currentBalance} must
 * never be edited independently of a ledger entry.</p>
 */
@Entity
@Table(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Wallet {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long walletId;

    /**
     * The student this wallet belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @ToString.Exclude
    private Student student;

    /**
     * Cached current wallet balance.
     *
     * <p>Must only ever be updated transactionally alongside a new
     * {@link WalletTransaction} row. {@link WalletTransaction} is the
     * authoritative source of truth for this value.</p>
     */
    @Column(name = "current_balance", precision = 12, scale = 2, nullable = false)
    private BigDecimal currentBalance;

    /**
     * Ledger of all top-up and debit operations performed on this
     * wallet.
     *
     * <p>Inverse side of the relationship; {@link WalletTransaction}
     * owns the foreign key. This is the authoritative, append-only
     * source of truth for the wallet's balance.</p>
     */
    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<WalletTransaction> walletTransactions = new ArrayList<>();
}
