package com.hostel.service.impl;

import com.hostel.entity.Bed;
import com.hostel.entity.Student;
import com.hostel.entity.StudentRoomHistory;
import com.hostel.enums.BedStatus;
import com.hostel.enums.StudentStatus;
import com.hostel.exception.BedOccupiedException;
import com.hostel.exception.DuplicateBookingException;
import com.hostel.exception.ResourceNotFoundException;
import com.hostel.repository.BedRepository;
import com.hostel.repository.StudentRepository;
import com.hostel.repository.StudentRoomHistoryRepository;
import com.hostel.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link StudentService}.
 *
 * <p>Contains all business logic for student booking, bed assignment,
 * search, checkout, cancellation, and yearly retention. Repository
 * details are not exposed beyond this class.</p>
 *
 * <p>This implementation deliberately has no dependency on
 * {@code CommercialPlanRepository}, {@code WalletRepository},
 * {@code FoodRepository}, or {@code InstallmentRepository} — retention
 * must never touch those tables, and omitting the dependencies entirely
 * removes any possibility of accidentally doing so.</p>
 */
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final BedRepository bedRepository;
    private final StudentRoomHistoryRepository studentRoomHistoryRepository;

    /**
     * Constructs a new {@code StudentServiceImpl}.
     *
     * @param studentRepository            the student repository
     * @param bedRepository                the bed repository, used to
     *                                     resolve and mutate bed
     *                                     assignment and status
     * @param studentRoomHistoryRepository the student room history
     *                                     repository, used to open and
     *                                     close history records
     */
    public StudentServiceImpl(StudentRepository studentRepository,
                               BedRepository bedRepository,
                               StudentRoomHistoryRepository studentRoomHistoryRepository) {
        this.studentRepository = studentRepository;
        this.bedRepository = bedRepository;
        this.studentRoomHistoryRepository = studentRoomHistoryRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student createStudentBooking(Student student) {
        Optional<Student> existing = studentRepository.findByBookingNo(student.getBookingNo());
        if (existing.isPresent()) {
            throw new DuplicateBookingException(student.getBookingNo());
        }

        student.setBed(null);
        return studentRepository.save(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student assignBed(Long studentId, Long bedId) {
        Student student = findStudentById(studentId);
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed", "id", bedId));

        if (bed.getStatus() == BedStatus.OCCUPIED) {
            throw new BedOccupiedException(bedId);
        }

        student.setBed(bed);
        bed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(bed);

        if (student.getStatus() == StudentStatus.CONFIRMED || student.getStatus() == StudentStatus.MID_SEASON) {
            student.setStatus(StudentStatus.ACTIVE);
        }

        Student savedStudent = studentRepository.save(student);

        StudentRoomHistory history = StudentRoomHistory.builder()
                .student(savedStudent)
                .room(bed.getRoom())
                .bed(bed)
                .fromDate(LocalDate.now())
                .toDate(null)
                .build();
        studentRoomHistoryRepository.save(history);

        return savedStudent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student updateStudent(Long studentId, Student student) {
        Student existingStudent = findStudentById(studentId);
        existingStudent.setName(student.getName());
        existingStudent.setGender(student.getGender());
        existingStudent.setDob(student.getDob());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setCourse(student.getCourse());
        return studentRepository.save(existingStudent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Student findStudentByBookingNo(String bookingNo) {
        return studentRepository.findByBookingNo(bookingNo)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "booking no", bookingNo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Student> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Student> findStudentsByRoomNo(String roomNo) {
        return studentRepository.findByRoomNo(roomNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Student> findStudentsByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Student findStudentByBed(Long bedId) {
        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new ResourceNotFoundException("Bed", "id", bedId));
        return studentRepository.findByBed(bed)
                .orElseThrow(() -> new ResourceNotFoundException("No student currently occupies bed id: " + bedId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student checkoutStudent(Long studentId) {
        Student student = findStudentById(studentId);
        releaseBedAndCloseHistory(student);
        student.setStatus(StudentStatus.CHECKED_OUT);
        student.setCheckOutDate(LocalDate.now());
        return studentRepository.save(student);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student cancelStudentBooking(Long studentId) {
        Student student = findStudentById(studentId);
        releaseBedAndCloseHistory(student);
        student.setStatus(StudentStatus.CANCELLED);
        student.setCheckOutDate(LocalDate.now());
        return studentRepository.save(student);
    }

    /**
     * Releases a student's currently assigned bed, if any, and closes
     * the corresponding open room history record.
     *
     * <p>Shared by {@link #checkoutStudent} and
     * {@link #cancelStudentBooking} so that bed-release and
     * history-closing behavior is implemented in exactly one place. If
     * the student has no bed assigned (e.g. a pre-move-in cancellation),
     * this method does nothing.</p>
     *
     * @param student the student whose bed assignment should be released
     */
    private void releaseBedAndCloseHistory(Student student) {
        Bed bed = student.getBed();
        if (bed == null) {
            return;
        }

        student.setBed(null);
        bed.setStatus(BedStatus.VACANT);
        bedRepository.save(bed);

        List<StudentRoomHistory> historyRecords = studentRoomHistoryRepository.findByStudent(student);
        historyRecords.stream()
                .filter(history -> history.getToDate() == null)
                .findFirst()
                .ifPresent(history -> {
                    history.setToDate(LocalDate.now());
                    studentRoomHistoryRepository.save(history);
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Student retainStudent(Long parentStudentId, Student newBookingDetails) {
        Student parentStudent = findStudentById(parentStudentId);

        Student retainedStudent = Student.builder()
                .parentStudent(parentStudent)
                .name(parentStudent.getName())
                .gender(parentStudent.getGender())
                .dob(parentStudent.getDob())
                .phone(parentStudent.getPhone())
                .email(parentStudent.getEmail())
                .course(parentStudent.getCourse())
                .bookingNo(newBookingDetails.getBookingNo())
                .legacyBookingNo(newBookingDetails.getLegacyBookingNo())
                .status(newBookingDetails.getStatus())
                .checkInDate(newBookingDetails.getCheckInDate())
                .checkOutDate(newBookingDetails.getCheckOutDate())
                .bed(null)
                .build();

        return studentRepository.save(retainedStudent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Student> findRetainedStudents(Long parentStudentId) {
        Student parentStudent = findStudentById(parentStudentId);
        return studentRepository.findByParentStudent(parentStudent);
    }
}
