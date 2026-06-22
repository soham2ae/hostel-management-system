package com.hostel.entity;

import com.hostel.enums.Gender;
import com.hostel.enums.StudentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single yearly booking record for a student.
 *
 * <p>The {@code student} table stores <b>yearly</b> booking information.
 * Every retained year creates a <b>new</b> {@code Student} row rather than
 * mutating the existing one, so that each year's booking, room, commercial
 * terms, installments, food, and wallet details remain independently
 * preserved as historical fact.</p>
 *
 * <p>Retention is modeled via the self-referencing {@link #parentStudent}
 * relationship: a retained-year row points back to the original student
 * row it was created from. Demographic fields (name, gender, dob, phone,
 * email, course) are copied automatically on retention; booking number,
 * room/bed, commercial amount, installments, food, and wallet details
 * must be entered manually for the new year.</p>
 *
 * <p>Upon checkout or cancellation, {@link #bed} is set to {@code null}
 * and the corresponding {@link Bed#getStatus()} is updated to
 * {@code VACANT}. The historical room assignment is preserved separately
 * via {@link StudentRoomHistory}, which is never deleted.</p>
 */
@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Student {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    /**
     * The prior year's student record this row was retained from, if any.
     *
     * <p>Self-referencing owning side; this entity holds the
     * {@code parent_student_id} foreign key column.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_student_id")
    @ToString.Exclude
    private Student parentStudent;

    /**
     * Subsequent yearly retention records created from this student.
     *
     * <p>Inverse side of the self-referencing relationship.</p>
     */
    @OneToMany(mappedBy = "parentStudent", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Student> retainedStudents = new ArrayList<>();

    /**
     * Booking number in the format {@code HOSTELCODE-YEAR-SEQUENCE}
     * (e.g. {@code JHU-2025-0001}).
     *
     * <p>Globally unique across all hostels and years.</p>
     */
    @Column(name = "booking_no", nullable = false, unique = true)
    private String bookingNo;

    /**
     * Legacy booking number retained for migration purposes only.
     *
     * <p>Nullable, unconstrained, and must never be used as a business
     * identifier.</p>
     */
    @Column(name = "legacy_booking_no")
    private String legacyBookingNo;

    /**
     * Student's full name.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Student's gender.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    /**
     * Student's date of birth.
     */
    @Column(name = "dob")
    private LocalDate dob;

    /**
     * Student's phone number.
     *
     * <p>Stored as a string, never a numeric type, to preserve leading
     * zeros, country code prefixes, and formatting.</p>
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Student's email address.
     */
    @Column(name = "email")
    private String email;

    /**
     * Course the student is enrolled in.
     */
    @Column(name = "course")
    private String course;

    /**
     * Date the student checked in for this booking year.
     */
    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    /**
     * Date the student checked out for this booking year, if applicable.
     */
    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    /**
     * Current lifecycle status of this booking.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentStatus status;

    /**
     * The bed currently occupied by this student for this booking year.
     *
     * <p>Owning side of the relationship; this entity holds the
     * foreign key column. Nullable to support checkout and cancellation,
     * at which point this is set to {@code null} and the bed's status
     * reverts to {@code VACANT}.</p>
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id")
    @ToString.Exclude
    private Bed bed;

    /**
     * Timestamp this record was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Timestamp this record was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Food record associated with this booking year.
     *
     * <p>Inverse side of the relationship; {@link Food} owns the
     * foreign key.</p>
     */
    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Food food;

    /**
     * Commercial plan associated with this booking year.
     *
     * <p>Inverse side of the relationship; {@link CommercialPlan} owns
     * the foreign key.</p>
     */
    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    @ToString.Exclude
    private CommercialPlan commercialPlan;

    /**
     * Wallet associated with this booking year.
     *
     * <p>Inverse side of the relationship; {@link Wallet} owns the
     * foreign key.</p>
     */
    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Wallet wallet;

    /**
     * Installments associated with this booking year.
     *
     * <p>Inverse side of the relationship; {@link Installment} owns
     * the foreign key.</p>
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Installment> installments = new ArrayList<>();

    /**
     * Payments associated with this booking year.
     *
     * <p>Inverse side of the relationship; {@link Payment} owns
     * the foreign key.</p>
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    /**
     * Food transactions associated with this booking year.
     *
     * <p>Inverse side of the relationship; {@link FoodTransaction} owns
     * the foreign key.</p>
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<FoodTransaction> foodTransactions = new ArrayList<>();

    /**
     * Room history records associated with this booking year.
     *
     * <p>Inverse side of the relationship; {@link StudentRoomHistory}
     * owns the foreign key. These records are append-only and must
     * never be deleted, even after checkout.</p>
     */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<StudentRoomHistory> studentRoomHistories = new ArrayList<>();
}
