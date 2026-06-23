package com.hostel.repository;

import com.hostel.entity.Payment;
import com.hostel.entity.Student;
import com.hostel.enums.PaymentMode;
import com.hostel.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Data access repository for {@link Payment}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>Aggregation methods on this repository are read-side projections
 * used for revenue reporting. They do not persist derived values —
 * nothing computed here is written back to the database.</p>
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds all payments made by a given student.
     *
     * @param student the student
     * @return payments made by the student
     */
    List<Payment> findByStudent(Student student);

    /**
     * Finds all payments of a given type.
     *
     * @param paymentType the payment type
     * @return payments of the given type
     */
    List<Payment> findByPaymentType(PaymentType paymentType);

    /**
     * Finds all payments made via a given mode.
     *
     * @param paymentMode the payment mode
     * @return payments made via the given mode
     */
    List<Payment> findByPaymentMode(PaymentMode paymentMode);

    /**
     * Finds all payments made within a given date range, inclusive.
     *
     * @param startDate the start of the range
     * @param endDate   the end of the range
     * @return payments made within the given range
     */
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates the total amount collected across all payments.
     *
     * <p>Read-side projection for revenue reporting; not a stored
     * value.</p>
     *
     * @return the total collected revenue, or {@code null} if there
     *         are no payments
     */
    @Query("SELECT SUM(p.amount) FROM Payment p")
    BigDecimal sumTotalCollectedRevenue();

    /**
     * Calculates the total amount collected within a given date range,
     * inclusive.
     *
     * <p>Read-side projection for revenue reporting; not a stored
     * value.</p>
     *
     * @param startDate the start of the range
     * @param endDate   the end of the range
     * @return the total collected revenue in the given range, or
     *         {@code null} if there are no matching payments
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal sumCollectedRevenueBetween(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    /**
     * Calculates the total amount collected, grouped by payment mode.
     *
     * <p>Read-side projection for revenue reporting; not a stored
     * value. Each result row is an {@code Object[]} of
     * {@code [PaymentMode, BigDecimal]}.</p>
     *
     * @return totals collected per payment mode
     */
    @Query("SELECT p.paymentMode, SUM(p.amount) FROM Payment p GROUP BY p.paymentMode")
    List<Object[]> sumCollectedRevenueByPaymentMode();
}
