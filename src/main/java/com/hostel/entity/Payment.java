package com.hostel.entity;

import com.hostel.enums.PaymentMode;
import com.hostel.enums.PaymentType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single payment transaction made by a {@link Student}.
 *
 * <p>One {@link Student} may have many {@code Payment} records.</p>
 *
 * <p>Online payment modes (UPI, Card, Bank Transfer) may have one or
 * more supporting screenshots attached via {@link PaymentAttachment}.</p>
 */
@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Payment {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    /**
     * The student this payment was made by.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    /**
     * Amount paid in this transaction.
     */
    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    /**
     * Date the payment was made.
     */
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    /**
     * Method used to make the payment.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;

    /**
     * Business reason the payment was made.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    /**
     * Transaction reference number, if applicable (e.g. UPI reference,
     * cheque number, bank transfer UTR).
     */
    @Column(name = "transaction_reference_no")
    private String transactionReferenceNo;

    /**
     * Free-text remarks about this payment.
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * Timestamp this record was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Screenshots or supporting documents attached to this payment.
     *
     * <p>Inverse side of the relationship; {@link PaymentAttachment}
     * owns the foreign key.</p>
     */
    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<PaymentAttachment> paymentAttachments = new ArrayList<>();
}
