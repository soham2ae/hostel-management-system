package com.hostel.repository;

import com.hostel.entity.Installment;
import com.hostel.entity.Student;
import com.hostel.enums.InstallmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access repository for {@link Installment}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p><b>Important:</b> "Overdue" is not a stored status and therefore
 * has no corresponding repository method. Overdue installments must be
 * derived in the service layer as
 * {@code status == PENDING && dueDate.isBefore(today)}, typically by
 * filtering the result of {@link #findByStatus} with
 * {@link InstallmentStatus#PENDING}.</p>
 */
public interface InstallmentRepository extends JpaRepository<Installment, Long> {

    /**
     * Finds all installments belonging to a given student.
     *
     * @param student the student
     * @return installments for the student
     */
    List<Installment> findByStudent(Student student);

    /**
     * Finds all installments with the given status.
     *
     * @param status the installment status
     * @return installments with the given status
     */
    List<Installment> findByStatus(InstallmentStatus status);
}
