package com.hostel.repository;

import com.hostel.entity.Payment;
import com.hostel.entity.PaymentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access repository for {@link PaymentAttachment}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 */
public interface PaymentAttachmentRepository extends JpaRepository<PaymentAttachment, Long> {

    /**
     * Finds all attachments belonging to a given payment.
     *
     * @param payment the payment
     * @return attachments for the payment
     */
    List<PaymentAttachment> findByPayment(Payment payment);
}
