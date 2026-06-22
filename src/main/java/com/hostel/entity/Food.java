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
 * Represents the food plan configured for a single {@link Student}
 * booking year.
 *
 * <p>One {@link Student} has exactly one {@code Food} record per
 * booking year. This record describes whether lunch is included and
 * the associated charges; actual billed/collected food revenue is
 * tracked separately and transactionally via {@link FoodTransaction}.</p>
 */
@Entity
@Table(name = "food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Food {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long foodId;

    /**
     * The student this food plan belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    @ToString.Exclude
    private Student student;

    /**
     * Whether lunch is included in this student's plan.
     */
    @Column(name = "lunch_included")
    private Boolean lunchIncluded;

    /**
     * Amount charged for lunch, if applicable.
     */
    @Column(name = "lunch_amount", precision = 12, scale = 2)
    private BigDecimal lunchAmount;

    /**
     * Amount charged for à la carte food, if applicable.
     */
    @Column(name = "alacarte_amount", precision = 12, scale = 2)
    private BigDecimal alacarteAmount;
}
