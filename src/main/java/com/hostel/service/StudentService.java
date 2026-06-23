package com.hostel.service;

import com.hostel.entity.Student;
import com.hostel.enums.StudentStatus;

import java.util.List;

/**
 * Service contract for managing {@link Student} records.
 *
 * <p>This is the most important service in the system. It governs
 * student booking creation, bed assignment, the universal search
 * feature (booking number, name, room number), and the full student
 * lifecycle: move-in, checkout, cancellation, and yearly retention.</p>
 *
 * <p>Booking creation and bed assignment are deliberately separate
 * operations: a student may exist in {@link StudentStatus#CONFIRMED}
 * state with no bed assigned yet. Bed assignment, checkout, and
 * cancellation are the only operations that ever create or close
 * {@link com.hostel.entity.StudentRoomHistory} records, which are
 * always preserved and never deleted.</p>
 */
public interface StudentService {

    /**
     * Creates a new student booking.
     *
     * <p>Does not assign a bed. A newly created booking has no bed and
     * no room history until {@link #assignBed} is called, allowing a
     * booking to exist in {@link StudentStatus#CONFIRMED} state prior
     * to move-in.</p>
     *
     * @param student the student booking to create
     * @return the created student, with its generated identifier populated
     */
    Student createStudentBooking(Student student);

    /**
     * Assigns a bed to a student, marking them as moved in.
     *
     * <p>Rejects the assignment if the target bed is already occupied
     * — an occupied bed is never silently reassigned. On success:</p>
     * <ul>
     *   <li>The bed is attached to the student and marked
     *       {@link com.hostel.enums.BedStatus#OCCUPIED}.</li>
     *   <li>A new {@link com.hostel.entity.StudentRoomHistory} record is
     *       created for this assignment, with an open-ended end date.</li>
     *   <li>If the student's current status is
     *       {@link StudentStatus#CONFIRMED} or
     *       {@link StudentStatus#MID_SEASON}, it is automatically
     *       transitioned to {@link StudentStatus#ACTIVE}.</li>
     * </ul>
     *
     * @param studentId the identifier of the student to assign a bed to
     * @param bedId     the identifier of the bed to assign
     * @return the updated student
     */
    Student assignBed(Long studentId, Long bedId);

    /**
     * Updates an existing student's details.
     *
     * @param studentId the identifier of the student to update
     * @param student   the updated student details
     * @return the updated student
     */
    Student updateStudent(Long studentId, Student student);

    /**
     * Finds a student by their identifier.
     *
     * @param studentId the student identifier
     * @return the matching student
     */
    Student findStudentById(Long studentId);

    /**
     * Finds a student by their unique booking number.
     *
     * <p>The highest-priority universal search criterion; returns
     * exactly one student.</p>
     *
     * @param bookingNo the booking number (e.g. "JHU-2025-0001")
     * @return the matching student
     */
    Student findStudentByBookingNo(String bookingNo);

    /**
     * Searches students by name, case-insensitively, matching anywhere
     * within the name.
     *
     * @param name the search text
     * @return students whose name contains the given text
     */
    List<Student> searchStudentsByName(String name);

    /**
     * Searches for all students currently occupying beds in rooms with
     * the given room number.
     *
     * <p>Room numbers are not globally unique, so this may return
     * students across multiple physical rooms sharing the same number.
     * Returns all students associated with that room, including beds
     * in different rooms that share the same room number.</p>
     *
     * @param roomNo the room number
     * @return students occupying beds in rooms with the given room number
     */
    List<Student> findStudentsByRoomNo(String roomNo);

    /**
     * Finds all students with the given status.
     *
     * @param status the student status
     * @return students with the given status
     */
    List<Student> findStudentsByStatus(StudentStatus status);

    /**
     * Finds the student currently occupying a specific bed.
     *
     * @param bedId the bed identifier
     * @return the occupying student
     */
    Student findStudentByBed(Long bedId);

    /**
     * Checks out a student, ending their current stay.
     *
     * <p>If the student currently occupies a bed, the bed is released
     * (set to {@link com.hostel.enums.BedStatus#VACANT}, detached from
     * the student) and the corresponding open
     * {@link com.hostel.entity.StudentRoomHistory} record is closed
     * with today's date. If the student has no bed assigned, only the
     * status transition is applied.</p>
     *
     * @param studentId the identifier of the student to check out
     * @return the updated student
     */
    Student checkoutStudent(Long studentId);

    /**
     * Cancels a student's booking.
     *
     * <p>Supports cancellation both before move-in (no bed assigned)
     * and after move-in (bed already occupied). In either case, any
     * currently assigned bed is released and its open room history
     * record is closed, using the same logic as {@link #checkoutStudent}.</p>
     *
     * @param studentId the identifier of the student whose booking is
     *                  being cancelled
     * @return the updated student
     */
    Student cancelStudentBooking(Long studentId);

    /**
     * Retains a student for a new booking year, creating a completely
     * new {@link Student} row.
     *
     * <p>Demographic fields (name, gender, date of birth, phone, email,
     * course) are always copied from the parent student record and
     * cannot be overridden by {@code newBookingDetails}. Only
     * booking-specific fields on {@code newBookingDetails} — such as
     * booking number, status, and check-in date — are honored.</p>
     *
     * <p>Commercial details, installments, wallet, food, and bed
     * assignment are never copied from the parent and must be created
     * independently through their respective services. No room history
     * record is created by this operation; the retained student
     * receives history only once {@link #assignBed} is later called for
     * the new booking year.</p>
     *
     * @param parentStudentId  the identifier of the student being retained
     * @param newBookingDetails booking-specific details for the new year
     * @return the newly created, retained student record
     */
    Student retainStudent(Long parentStudentId, Student newBookingDetails);

    /**
     * Finds all students retained from a given parent student record.
     *
     * @param parentStudentId the identifier of the original student record
     * @return retained students linked to the given parent
     */
    List<Student> findRetainedStudents(Long parentStudentId);
}
