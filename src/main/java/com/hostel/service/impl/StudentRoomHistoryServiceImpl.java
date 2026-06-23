package com.hostel.service.impl;

import com.hostel.entity.Room;
import com.hostel.entity.Student;
import com.hostel.entity.StudentRoomHistory;
import com.hostel.repository.RoomRepository;
import com.hostel.repository.StudentRepository;
import com.hostel.repository.StudentRoomHistoryRepository;
import com.hostel.service.StudentRoomHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link StudentRoomHistoryService}.
 *
 * <p>Contains all business logic for retrieving {@link StudentRoomHistory}
 * records. Repository details are not exposed beyond this class.</p>
 *
 * <p>This class contains no create, update, or delete methods. Room
 * history records are written exclusively by {@code StudentServiceImpl}
 * as part of bed assignment and checkout/cancellation, and are never
 * deleted. Immutability is enforced here simply by the absence of any
 * mutating capability — not by guard methods that throw.</p>
 */
@Service
public class StudentRoomHistoryServiceImpl implements StudentRoomHistoryService {

    private final StudentRoomHistoryRepository studentRoomHistoryRepository;
    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;

    /**
     * Constructs a new {@code StudentRoomHistoryServiceImpl}.
     *
     * @param studentRoomHistoryRepository the student room history
     *                                     repository
     * @param studentRepository            the student repository, used
     *                                     to resolve a student when
     *                                     searching history by student
     * @param roomRepository               the room repository, used to
     *                                     resolve a room when searching
     *                                     history by room
     */
    public StudentRoomHistoryServiceImpl(StudentRoomHistoryRepository studentRoomHistoryRepository,
                                          StudentRepository studentRepository,
                                          RoomRepository roomRepository) {
        this.studentRoomHistoryRepository = studentRoomHistoryRepository;
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public StudentRoomHistory findHistoryById(Long historyId) {
        return studentRoomHistoryRepository.findById(historyId)
                .orElseThrow(() -> new RuntimeException("Room history record not found with id: " + historyId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentRoomHistory> findHistoryByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return studentRoomHistoryRepository.findByStudent(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentRoomHistory> findHistoryByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        return studentRoomHistoryRepository.findByRoom(room);
    }
}
