package com.hostel.service.impl;

import com.hostel.entity.Payment;
import com.hostel.entity.PaymentAttachment;
import com.hostel.entity.Student;
import com.hostel.enums.PaymentMode;
import com.hostel.enums.PaymentType;
import com.hostel.repository.PaymentAttachmentRepository;
import com.hostel.repository.PaymentRepository;
import com.hostel.repository.StudentRepository;
import com.hostel.service.InstallmentService;
import com.hostel.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Default implementation of {@link PaymentService}.
 *
 * <p>Contains all business logic for recording payments and managing
 * their attachments. Repository details are not exposed beyond this
 * class.</p>
 *
 * <p>This implementation deliberately has no dependency on
 * {@code WalletRepository} or {@code WalletService} — payments and
 * wallet bookkeeping remain entirely separate concerns.</p>
 *
 * <p>When recording a payment of type
 * {@link PaymentType#INSTALLMENT}, this service delegates to
 * {@link InstallmentService#markInstallmentAsPaid(Long)} rather than
 * depending on {@code InstallmentRepository} directly or mutating an
 * {@link com.hostel.entity.Installment} entity itself.
 * {@code InstallmentService} remains the sole owner of installment
 * business rules.</p>
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentAttachmentRepository paymentAttachmentRepository;
    private final StudentRepository studentRepository;
    private final InstallmentService installmentService;

    /**
     * Constructs a new {@code PaymentServiceImpl}.
     *
     * @param paymentRepository           the payment repository
     * @param paymentAttachmentRepository the payment attachment
     *                                    repository
     * @param studentRepository           the student repository, used
     *                                    to resolve the owning student
     *                                    when recording a payment
     * @param installmentService         the installment service, used
     *                                    to mark the corresponding
     *                                    installment as paid when an
     *                                    {@code INSTALLMENT}-type
     *                                    payment is recorded
     */
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                               PaymentAttachmentRepository paymentAttachmentRepository,
                               StudentRepository studentRepository,
                               InstallmentService installmentService) {
        this.paymentRepository = paymentRepository;
        this.paymentAttachmentRepository = paymentAttachmentRepository;
        this.studentRepository = studentRepository;
        this.installmentService = installmentService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Payment recordPayment(Long studentId, Payment payment, Long installmentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        payment.setStudent(student);
        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getPaymentType() == PaymentType.INSTALLMENT) {
            if (installmentId == null) {
                throw new RuntimeException(
                        "installmentId is required when recording a payment of type INSTALLMENT");
            }
            installmentService.markInstallmentAsPaid(installmentId);
        }

        return savedPayment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PaymentAttachment addPaymentAttachment(Long paymentId, PaymentAttachment attachment) {
        Payment payment = findPaymentById(paymentId);
        attachment.setPayment(payment);
        return paymentAttachmentRepository.save(attachment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Payment findPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findPaymentsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return paymentRepository.findByStudent(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findPaymentsByType(PaymentType paymentType) {
        return paymentRepository.findByPaymentType(paymentType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findPaymentsByMode(PaymentMode paymentMode) {
        return paymentRepository.findByPaymentMode(paymentMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findPaymentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentAttachment> findAttachmentsByPayment(Long paymentId) {
        Payment payment = findPaymentById(paymentId);
        return paymentAttachmentRepository.findByPayment(payment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalCollectedRevenue() {
        BigDecimal total = paymentRepository.sumTotalCollectedRevenue();
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCollectedRevenueBetweenDates(LocalDate startDate, LocalDate endDate) {
        BigDecimal total = paymentRepository.sumCollectedRevenueBetween(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }
}
