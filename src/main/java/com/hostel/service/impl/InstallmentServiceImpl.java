package com.hostel.service.impl;

import com.hostel.entity.Installment;
import com.hostel.entity.Student;
import com.hostel.enums.InstallmentStatus;
import com.hostel.exception.InstallmentOwnershipException;
import com.hostel.exception.ResourceNotFoundException;
import com.hostel.repository.InstallmentRepository;
import com.hostel.repository.StudentRepository;
import com.hostel.service.InstallmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link InstallmentService}.
 *
 * <p>Contains all business logic for creating, updating, and
 * retrieving {@link Installment} records, including derivation of
 * overdue status. Repository details are not exposed beyond this
 * class.</p>
 *
 * <p><b>Important:</b> "Overdue" is never persisted. It is computed on
 * every read as {@code status == PENDING && dueDate.isBefore(today)}.</p>
 */
@Service
public class InstallmentServiceImpl implements InstallmentService {

    private final InstallmentRepository installmentRepository;
    private final StudentRepository studentRepository;

    /**
     * Constructs a new {@code InstallmentServiceImpl}.
     *
     * @param installmentRepository the installment repository
     * @param studentRepository     the student repository, used to
     *                              resolve the owning student when
     *                              creating an installment
     */
    public InstallmentServiceImpl(InstallmentRepository installmentRepository,
                                   StudentRepository studentRepository) {
        this.installmentRepository = installmentRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Installment createInstallment(Long studentId, Installment installment) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        installment.setStudent(student);
        return installmentRepository.save(installment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Installment updateInstallment(Long installmentId, Installment installment) {
        Installment existingInstallment = findInstallmentById(installmentId);
        existingInstallment.setInstallmentNo(installment.getInstallmentNo());
        existingInstallment.setAmount(installment.getAmount());
        existingInstallment.setDueDate(installment.getDueDate());
        existingInstallment.setStatus(installment.getStatus());
        return installmentRepository.save(existingInstallment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Installment findInstallmentById(Long installmentId) {
        return installmentRepository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Installment", "id", installmentId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Installment> findInstallmentsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return installmentRepository.findByStudent(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Installment> findInstallmentsByStatus(InstallmentStatus status) {
        return installmentRepository.findByStatus(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Installment> findOverdueInstallments() {
        LocalDate today = LocalDate.now();
        return installmentRepository.findByStatus(InstallmentStatus.PENDING).stream()
                .filter(installment -> installment.getDueDate() != null
                        && installment.getDueDate().isBefore(today))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isOverdue(Long installmentId) {
        Installment installment = findInstallmentById(installmentId);
        return installment.getStatus() == InstallmentStatus.PENDING
                && installment.getDueDate() != null
                && installment.getDueDate().isBefore(LocalDate.now());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Installment markInstallmentAsPaid(Long studentId, Long installmentId) {
        Installment installment = findInstallmentById(installmentId);

        if (!installment.getStudent().getStudentId().equals(studentId)) {
            throw new InstallmentOwnershipException(installmentId, studentId);
        }

        installment.setStatus(InstallmentStatus.PAID);
        return installmentRepository.save(installment);
    }
}
