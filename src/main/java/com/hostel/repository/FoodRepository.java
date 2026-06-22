package com.hostel.repository;

import com.hostel.entity.Food;
import com.hostel.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data access repository for {@link Food}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 */
public interface FoodRepository extends JpaRepository<Food, Long> {

    /**
     * Finds the food record belonging to a given student.
     *
     * @param student the student
     * @return the matching food record, if any
     */
    Optional<Food> findByStudent(Student student);
}
