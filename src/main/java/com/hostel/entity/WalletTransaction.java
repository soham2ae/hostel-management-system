package com.hostel.entity;

import com.hostel.enums.WalletTransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a single append-only ledger entry for a {@link Wallet}.
 *
 * <p>This is the authoritative source of truth for a wallet's balance;
 * {@link Wallet#getCurrentBalance()} is only ever a cached value derived
 * from this ledger.</p>
 *
 * <p>Field population depends on {@link #transactionType}:</p>
 * <ul>
 *   <li>{@link WalletTransactionType#TOPUP} — {@link #actualAmountPaid}
 *       and {@link #creditAmount} are populated; {@link #transactionAmount}
 *       is {@code null}. Example: student pays ₹10,000 and is credited
 *       ₹15,000.</li>
 *   <li>{@link WalletTransactionType#DEBIT} — {@link #transactionAmount}
 *       is populated; {@link #actualAmountPaid} and {@link #creditAmount}
 *       are {@code null}. Example: student spends ₹3,000 from the
 *       wallet.</li>
 * </ul>
 */
@Entity
@Table(name = "wallet_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WalletTransaction {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_transaction_id")
    private Long walletTransactionId;

    /**
     * The wallet this transaction belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    @ToString.Exclude
    private Wallet wallet;

    /**
     * The type of wallet operation this transaction represents.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private WalletTransactionType transactionType;

    /**
     * Amount physically paid by the student. Populated only for
     * {@link WalletTransactionType#TOPUP} transactions.
     */
    @Column(name = "actual_amount_paid", precision = 12, scale = 2)
    private BigDecimal actualAmountPaid;

    /**
     * Amount credited to the wallet. Populated only for
     * {@link WalletTransactionType#TOPUP} transactions.
     */
    @Column(name = "credit_amount", precision = 12, scale = 2)
    private BigDecimal creditAmount;

    /**
     * Amount consumed from the wallet. Populated only for
     * {@link WalletTransactionType#DEBIT} transactions.
     */
    @Column(name = "transaction_amount", precision = 12, scale = 2)
    private BigDecimal transactionAmount;

    /**
     * Date and time this transaction occurred.
     */
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
}
