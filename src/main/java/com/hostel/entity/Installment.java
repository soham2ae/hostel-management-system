package com.hostel.entity;

import com.hostel.enums.InstallmentStatus;
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
import java.time.LocalDate;

/**
 * Represents a single installment due for a {@link Student} booking
 * year.
 *
 * <p>One {@link Student} may have many {@code Installment} records.</p>
 *
 * <p><b>Important:</b> {@link #status} only ever stores
 * {@link InstallmentStatus#PENDING} or {@link InstallmentStatus#PAID}.
 * "Overdue" is never stored — it is a derived condition, computed
 * dynamically wherever needed as
 * {@code status == PENDING && dueDate.isBefore(today)}.</p>
 */
@Entity
@Table(name = "installment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Installment {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "installment_id")
    private Long installmentId;

    /**
     * The student this installment belongs to.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    /**
     * Sequential number of this installment within the student's plan
     * (e.g. 1, 2, 3).
     */
    @Column(name = "installment_no", nullable = false)
    private Integer installmentNo;

    /**
     * Amount due for this installment.
     */
    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    /**
     * Date this installment is due.
     */
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    /**
     * Current stored status of this installment.
     *
     * <p>Only {@code PENDING} or {@code PAID} are ever stored.
     * "Overdue" is derived, not stored.</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InstallmentStatus status;
}
