package com.hostel.repository;

import com.hostel.entity.Room;
import com.hostel.entity.Student;
import com.hostel.entity.StudentRoomHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access repository for {@link StudentRoomHistory}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p><b>Important:</b> room history records are append-only and must
 * never be deleted. This repository intentionally exposes no
 * delete-related methods beyond what {@link JpaRepository} provides
 * by default — callers must not invoke {@code delete}, {@code deleteById},
 * {@code deleteAll}, or any other inherited delete operation on this
 * repository under any circumstance.</p>
 */
public interface StudentRoomHistoryRepository extends JpaRepository<StudentRoomHistory, Long> {

    /**
     * Finds all room history records belonging to a given student.
     *
     * @param student the student
     * @return room history records for the student
     */
    List<StudentRoomHistory> findByStudent(Student student);

    /**
     * Finds all room history records associated with a given room.
     *
     * @param room the room
     * @return room history records for the room
     */
    List<StudentRoomHistory> findByRoom(Room room);
}
