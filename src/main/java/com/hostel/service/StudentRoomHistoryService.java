package com.hostel.service;

import com.hostel.entity.StudentRoomHistory;

import java.util.List;

/**
 * Service contract for retrieving {@link StudentRoomHistory} records.
 *
 * <p>Room history is immutable. Records are created and closed
 * exclusively as part of {@code StudentService}'s bed assignment and
 * checkout/cancellation workflows, and are never deleted under any
 * circumstance.</p>
 *
 * <p>This service intentionally exposes no create, update, or delete
 * operations whatsoever — not even ones that would throw if called.
 * Room history immutability is enforced simply by never providing the
 * capability to mutate or remove it through this service.</p>
 */
public interface StudentRoomHistoryService {

    /**
     * Finds a room history record by its identifier.
     *
     * @param historyId the room history record identifier
     * @return the matching room history record
     */
    StudentRoomHistory findHistoryById(Long historyId);

    /**
     * Finds all room history records belonging to a given student.
     *
     * @param studentId the student identifier
     * @return room history records for the student
     */
    List<StudentRoomHistory> findHistoryByStudent(Long studentId);

    /**
     * Finds all room history records associated with a given room.
     *
     * @param roomId the room identifier
     * @return room history records for the room
     */
    List<StudentRoomHistory> findHistoryByRoom(Long roomId);
}
