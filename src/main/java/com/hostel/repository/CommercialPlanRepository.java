package com.hostel.repository;

import com.hostel.entity.CommercialPlan;
import com.hostel.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Data access repository for {@link CommercialPlan}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 */
public interface CommercialPlanRepository extends JpaRepository<CommercialPlan, Long> {

    /**
     * Finds the commercial plan belonging to a given student.
     *
     * @param student the student
     * @return the matching commercial plan, if any
     */
    Optional<CommercialPlan> findByStudent(Student student);

    /**
     * Calculates the total commercial amount across all commercial
     * plans.
     *
     * <p>Read-side projection used for dashboard revenue reporting; not
     * a stored value.</p>
     *
     * @return the total commercial amount, or {@code null} if there
     *         are no commercial plans
     */
    @Query("SELECT SUM(cp.commercialAmount) FROM CommercialPlan cp")
    BigDecimal sumTotalCommercialAmount();
}
