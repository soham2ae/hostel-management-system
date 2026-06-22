package com.hostel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Represents a single uploaded screenshot or supporting document for a
 * {@link Payment}.
 *
 * <p>One {@link Payment} may have many {@code PaymentAttachment}
 * records, typically used for online payment modes (UPI, Card, Bank
 * Transfer).</p>
 *
 * <p><b>Important:</b> the actual file binary is never stored in the
 * database. Files reside on the local filesystem under
 * {@code uploads/payments}; only {@link #filePath} is persisted here.</p>
 */
@Entity
@Table(name = "payment_attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentAttachment {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long attachmentId;

    /**
     * The payment this attachment supports.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    @ToString.Exclude
    private Payment payment;

    /**
     * Original uploaded file name.
     */
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * Path to the stored file on the local filesystem
     * (under {@code uploads/payments}).
     *
     * <p>Only the path is stored here — never the file's binary
     * content.</p>
     */
    @Column(name = "file_path", nullable = false)
    private String filePath;

    /**
     * Timestamp this attachment was uploaded.
     */
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
}
