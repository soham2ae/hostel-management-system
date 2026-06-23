package com.hostel.service;

import com.hostel.entity.Payment;
import com.hostel.entity.PaymentAttachment;
import com.hostel.enums.PaymentMode;
import com.hostel.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service contract for recording {@link Payment} transactions and
 * managing their {@link PaymentAttachment} screenshots.
 *
 * <p>Supports all payment types: {@link PaymentType#BOOKING},
 * {@link PaymentType#MOVE_IN}, {@link PaymentType#INSTALLMENT},
 * {@link PaymentType#FOOD}, and {@link PaymentType#A_LA_CARTE}.</p>
 *
 * <p>Payment attachments store only a file path; the underlying image
 * binary is never persisted to the database.</p>
 *
 * <p>This service is entirely separate from
 * {@link com.hostel.entity.Wallet} bookkeeping and has no dependency
 * on wallet-related repositories or services.</p>
 */
public interface PaymentService {

    /**
     * Records a new payment for a student.
     *
     * <p>Resolves the owning {@link com.hostel.entity.Student} from
     * {@code studentId} and attaches it to the payment before
     * persisting, so that callers (typically controllers) need only
     * supply the parent identifier rather than a hydrated
     * {@link com.hostel.entity.Student} entity.</p>
     *
     * <p>Payments do not reference an installment directly in the
     * schema. When {@code payment.getPaymentType()} is
     * {@link PaymentType#INSTALLMENT}, {@code installmentId} is
     * required and identifies the specific installment being settled;
     * this service delegates to
     * {@code InstallmentService.markInstallmentAsPaid(Long)} to apply
     * that update. No assumption is made about which installment is
     * being paid — the caller must always specify it explicitly; this
     * service never infers it (e.g. by oldest installment or nearest
     * due date).</p>
     *
     * <p>For all other payment types ({@link PaymentType#BOOKING},
     * {@link PaymentType#MOVE_IN}, {@link PaymentType#FOOD},
     * {@link PaymentType#A_LA_CARTE}), {@code installmentId} may be
     * {@code null} and is ignored.</p>
     *
     * @param studentId    the identifier of the student making the payment
     * @param payment      the payment to record
     * @param installmentId the identifier of the installment being
     *                      settled; required when the payment type is
     *                      {@link PaymentType#INSTALLMENT}, otherwise
     *                      ignored
     * @return the recorded payment, with its generated identifier populated
     */
    Payment recordPayment(Long studentId, Payment payment, Long installmentId);

    /**
     * Adds an attachment (e.g. a payment screenshot) to an existing
     * payment.
     *
     * <p>Resolves the owning {@link Payment} from {@code paymentId} and
     * attaches it to the attachment record before persisting. Only the
     * file path is stored; the binary content of the uploaded file must
     * already reside on disk under the configured payments directory
     * before this method is called.</p>
     *
     * @param paymentId  the identifier of the payment this attachment
     *                   supports
     * @param attachment the attachment to add, with {@code fileName}
     *                   and {@code filePath} populated
     * @return the created attachment, with its generated identifier populated
     */
    PaymentAttachment addPaymentAttachment(Long paymentId, PaymentAttachment attachment);

    /**
     * Finds a payment by its identifier.
     *
     * @param paymentId the payment identifier
     * @return the matching payment
     */
    Payment findPaymentById(Long paymentId);

    /**
     * Finds all payments made by a given student.
     *
     * @param studentId the student identifier
     * @return payments made by the student
     */
    List<Payment> findPaymentsByStudent(Long studentId);

    /**
     * Finds all payments of a given type.
     *
     * @param paymentType the payment type
     * @return payments of the given type
     */
    List<Payment> findPaymentsByType(PaymentType paymentType);

    /**
     * Finds all payments made via a given mode.
     *
     * @param paymentMode the payment mode
     * @return payments made via the given mode
     */
    List<Payment> findPaymentsByMode(PaymentMode paymentMode);

    /**
     * Finds all payments made within a given date range, inclusive.
     *
     * @param startDate the start of the range
     * @param endDate   the end of the range
     * @return payments made within the given range
     */
    List<Payment> findPaymentsBetweenDates(LocalDate startDate, LocalDate endDate);

    /**
     * Finds all attachments belonging to a given payment.
     *
     * @param paymentId the payment identifier
     * @return attachments for the payment
     */
    List<PaymentAttachment> findAttachmentsByPayment(Long paymentId);

    /**
     * Calculates the total revenue collected across all payments.
     *
     * @return the total collected revenue, never {@code null}
     *         ({@link BigDecimal#ZERO} when there are no payments)
     */
    BigDecimal getTotalCollectedRevenue();

    /**
     * Calculates the total revenue collected within a given date
     * range, inclusive.
     *
     * @param startDate the start of the range
     * @param endDate   the end of the range
     * @return the total collected revenue in the given range, never
     *         {@code null} ({@link BigDecimal#ZERO} when there are no
     *         matching payments)
     */
    BigDecimal getCollectedRevenueBetweenDates(LocalDate startDate, LocalDate endDate);
}
