package com.hostel.repository;

import com.hostel.entity.CommercialPlan;
import com.hostel.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
