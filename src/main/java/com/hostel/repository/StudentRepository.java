package com.hostel.repository;

import com.hostel.entity.Bed;
import com.hostel.entity.Room;
import com.hostel.entity.Student;
import com.hostel.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Student}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>This is one of the most important repositories in the system. It
 * backs the universal search feature, prioritized as:</p>
 * <ol>
 *   <li>Booking Number — {@link #findByBookingNo}</li>
 *   <li>Student Name — {@link #findByNameContainingIgnoreCase}</li>
 *   <li>Room Number — {@link #findByRoomNo}</li>
 * </ol>
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Finds a student by their unique booking number.
     *
     * @param bookingNo the booking number (e.g. "JHU-2025-0001")
     * @return the matching student, if any
     */
    Optional<Student> findByBookingNo(String bookingNo);

    /**
     * Finds students with an exact name match.
     *
     * @param name the student name
     * @return students matching the given name
     */
    List<Student> findByName(String name);

    /**
     * Searches students by name, case-insensitively, matching anywhere
     * within the name.
     *
     * @param name the search text
     * @return students whose name contains the given text
     */
    List<Student> findByNameContainingIgnoreCase(String name);

    /**
     * Finds students with the given status.
     *
     * @param status the student status
     * @return students with the given status
     */
    List<Student> findByStatus(StudentStatus status);

    /**
     * Finds the student currently occupying a specific bed.
     *
     * @param bed the bed
     * @return the occupying student, if any
     */
    Optional<Student> findByBed(Bed bed);

    /**
     * Finds all students currently occupying beds within a given room.
     *
     * @param room the room
     * @return students occupying beds in the room
     */
    List<Student> findByBedRoom(Room room);

    /**
     * Finds all students retained from a given parent student record.
     *
     * @param parentStudent the original student record
     * @return retained students linked to the given parent
     */
    List<Student> findByParentStudent(Student parentStudent);

    /**
     * Finds all students that are themselves retentions of a prior
     * booking year (i.e. have a non-null parent student).
     *
     * @return retained students
     */
    List<Student> findByParentStudentIsNotNull();

    /**
     * Searches for all students currently occupying beds in a room
     * identified by room number, traversing
     * {@code Student -> Bed -> Room}.
     *
     * <p>Room numbers are not globally unique, so this may span
     * multiple physical rooms (e.g. on different floors) sharing the
     * same room number.</p>
     *
     * @param roomNo the room number
     * @return students occupying beds in rooms with the given room number
     */
    @Query("SELECT s FROM Student s WHERE s.bed.room.roomNo = :roomNo")
    List<Student> findByRoomNo(@Param("roomNo") String roomNo);
}
