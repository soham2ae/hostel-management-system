package com.hostel.service;

import com.hostel.entity.Installment;
import com.hostel.enums.InstallmentStatus;

import java.util.List;

/**
 * Service contract for managing {@link Installment} records.
 *
 * <p>Defines the business operations available for creating, updating,
 * and retrieving installments.</p>
 *
 * <p><b>Important:</b> "Overdue" is never a stored status. It is
 * always derived dynamically as
 * {@code status == PENDING && dueDate.isBefore(today)}. This service
 * exposes that derivation via {@link #findOverdueInstallments} and
 * {@link #isOverdue}, but the underlying {@link InstallmentStatus} of
 * an overdue installment remains {@link InstallmentStatus#PENDING}.</p>
 */
public interface InstallmentService {

    /**
     * Creates a new installment for a student.
     *
     * <p>Resolves the owning {@link com.hostel.entity.Student} from
     * {@code studentId} and attaches it to the installment before
     * persisting, so that callers (typically controllers) need only
     * supply the parent identifier rather than a hydrated
     * {@link com.hostel.entity.Student} entity.</p>
     *
     * @param studentId   the identifier of the student this
     *                    installment belongs to
     * @param installment the installment to create
     * @return the created installment, with its generated identifier populated
     */
    Installment createInstallment(Long studentId, Installment installment);

    /**
     * Updates an existing installment.
     *
     * @param installmentId the identifier of the installment to update
     * @param installment   the updated installment details
     * @return the updated installment
     */
    Installment updateInstallment(Long installmentId, Installment installment);

    /**
     * Finds an installment by its identifier.
     *
     * @param installmentId the installment identifier
     * @return the matching installment
     */
    Installment findInstallmentById(Long installmentId);

    /**
     * Finds all installments belonging to a given student.
     *
     * @param studentId the student identifier
     * @return installments for the student
     */
    List<Installment> findInstallmentsByStudent(Long studentId);

    /**
     * Finds all installments with the given stored status.
     *
     * @param status the installment status ({@code PENDING} or
     *               {@code PAID})
     * @return installments with the given status
     */
    List<Installment> findInstallmentsByStatus(InstallmentStatus status);

    /**
     * Finds all installments that are currently overdue.
     *
     * <p>An installment is overdue when its stored status is
     * {@link InstallmentStatus#PENDING} and its due date is before
     * today. This is computed dynamically; no installment is ever
     * persisted with an overdue status.</p>
     *
     * @return installments that are currently overdue
     */
    List<Installment> findOverdueInstallments();

    /**
     * Determines whether a specific installment is currently overdue.
     *
     * @param installmentId the installment identifier
     * @return {@code true} if the installment's status is
     *         {@code PENDING} and its due date is before today;
     *         {@code false} otherwise
     */
    boolean isOverdue(Long installmentId);

    /**
     * Marks a specific installment as paid, after verifying it belongs
     * to the given student.
     *
     * <p>Loads the installment and verifies that
     * {@code installment.getStudent().getStudentId()} matches
     * {@code studentId} before applying any change. If the installment
     * does not belong to the given student, the operation is rejected
     * and the installment's status is left unchanged. Only once
     * ownership is confirmed is the installment's stored status set to
     * {@link InstallmentStatus#PAID}.</p>
     *
     * <p>This is the sole entry point by which other services (e.g.
     * payment recording) may settle an installment; callers outside
     * this service must never mutate an {@link Installment} entity's
     * status directly.</p>
     *
     * @param studentId      the identifier of the student expected to
     *                       own the installment
     * @param installmentId the identifier of the installment to mark
     *                      as paid
     * @return the updated installment
     */
    Installment markInstallmentAsPaid(Long studentId, Long installmentId);
}
