package com.hostel.entity;

import com.hostel.enums.FoodType;
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

/**
 * Represents a single food-related revenue transaction for a
 * {@link Student}.
 *
 * <p>Tracks the amount billed for a food charge separately from the
 * amount actually paid, allowing billed, collected, and pending food
 * revenue to be reconciled. Optionally links to the {@link Payment}
 * that settled (fully or partially) this charge.</p>
 *
 * <p><b>Important:</b> {@link FoodType} is intentionally a separate
 * enum from {@link com.hostel.enums.PaymentType}. This entity's
 * {@link #foodType} describes the food service generating the charge,
 * not why money was paid.</p>
 */
@Entity
@Table(name = "food_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FoodTransaction {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_transaction_id")
    private Long foodTransactionId;

    /**
     * The student this food transaction belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    /**
     * The payment that settled this food charge, if any.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @ToString.Exclude
    private Payment payment;

    /**
     * The food service generating this charge.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false)
    private FoodType foodType;

    /**
     * Amount billed for this food charge.
     */
    @Column(name = "amount_billed", precision = 12, scale = 2, nullable = false)
    private BigDecimal amountBilled;

    /**
     * Amount actually paid against this food charge.
     */
    @Column(name = "amount_paid", precision = 12, scale = 2)
    private BigDecimal amountPaid;
}
