package com.hostel.service.impl;

import com.hostel.entity.Bed;
import com.hostel.entity.Room;
import com.hostel.repository.BedRepository;
import com.hostel.repository.RoomRepository;
import com.hostel.service.BedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link BedService}.
 *
 * <p>Contains all business logic for creating, updating, and
 * retrieving {@link Bed} records. Repository details are not exposed
 * beyond this class.</p>
 *
 * <p>Bed status transitions tied to student lifecycle events (move-in,
 * checkout, retention) are intentionally not exposed here — they are
 * orchestrated by {@code StudentService} alongside the corresponding
 * student-side changes, to keep bed and student occupancy state from
 * drifting out of sync.</p>
 */
@Service
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;

    /**
     * Constructs a new {@code BedServiceImpl}.
     *
     * @param bedRepository  the bed repository
     * @param roomRepository the room repository, used to resolve the
     *                       owning room when creating a bed
     */
    public BedServiceImpl(BedRepository bedRepository, RoomRepository roomRepository) {
        this.bedRepository = bedRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Bed createBed(Long roomId, Bed bed) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        bed.setRoom(room);
        return bedRepository.save(bed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Bed updateBed(Long bedId, Bed bed) {
        Bed existingBed = findBedById(bedId);
        existingBed.setBedNo(bed.getBedNo());
        return bedRepository.save(existingBed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Bed findBedById(Long bedId) {
        return bedRepository.findById(bedId)
                .orElseThrow(() -> new RuntimeException("Bed not found with id: " + bedId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Bed> findBedsByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        return bedRepository.findByRoom(room);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Bed> findVacantBeds() {
        return bedRepository.findVacantBeds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Bed> findOccupiedBeds() {
        return bedRepository.findOccupiedBeds();
    }
}
