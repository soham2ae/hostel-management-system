package com.hostel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Represents the commercial (financial) terms agreed for a single
 * {@link Student} booking year.
 *
 * <p>One {@link Student} has exactly one {@code CommercialPlan} record
 * per booking year. The security deposit is already included within
 * {@link #commercialAmount} and is not tracked separately.</p>
 *
 * <p>Pending amount is a derived value and must be calculated
 * dynamically from {@link #commercialAmount} against the sum of
 * collected payments — it must never be stored on this entity.</p>
 */
@Entity
@Table(name = "commercial_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommercialPlan {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    /**
     * The student this commercial plan belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @ToString.Exclude
    private Student student;

    /**
     * Total commercial amount agreed for the booking year, inclusive
     * of the security deposit.
     */
    @Column(name = "commercial_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal commercialAmount;

    /**
     * Amount paid at the time of booking, if any.
     */
    @Column(name = "booking_amount", precision = 12, scale = 2)
    private BigDecimal bookingAmount;

    /**
     * Amount paid at the time of move-in, if any.
     */
    @Column(name = "movein_amount", precision = 12, scale = 2)
    private BigDecimal moveinAmount;

    /**
     * Number of installments agreed for the remaining balance.
     */
    @Column(name = "installment_count")
    private Integer installmentCount;
}
