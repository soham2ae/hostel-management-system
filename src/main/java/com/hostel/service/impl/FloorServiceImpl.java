package com.hostel.service.impl;

import com.hostel.entity.Floor;
import com.hostel.entity.Hostel;
import com.hostel.repository.FloorRepository;
import com.hostel.repository.HostelRepository;
import com.hostel.service.FloorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link FloorService}.
 *
 * <p>Contains all business logic for creating, updating, and
 * retrieving {@link Floor} records. Repository details are not
 * exposed beyond this class.</p>
 */
@Service
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;
    private final HostelRepository hostelRepository;

    /**
     * Constructs a new {@code FloorServiceImpl}.
     *
     * @param floorRepository  the floor repository
     * @param hostelRepository the hostel repository, used to resolve
     *                         the owning hostel when creating a floor
     */
    public FloorServiceImpl(FloorRepository floorRepository, HostelRepository hostelRepository) {
        this.floorRepository = floorRepository;
        this.hostelRepository = hostelRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Floor createFloor(Long hostelId, Floor floor) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new RuntimeException("Hostel not found with id: " + hostelId));
        floor.setHostel(hostel);
        return floorRepository.save(floor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Floor updateFloor(Long floorId, Floor floor) {
        Floor existingFloor = findFloorById(floorId);
        existingFloor.setFloorNo(floor.getFloorNo());
        existingFloor.setTotalRooms(floor.getTotalRooms());
        return floorRepository.save(existingFloor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Floor findFloorById(Long floorId) {
        return floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + floorId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Floor> findFloorsByHostel(Long hostelId) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new RuntimeException("Hostel not found with id: " + hostelId));
        return floorRepository.findByHostel(hostel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Floor findFloorByHostelAndFloorNo(Long hostelId, Integer floorNo) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new RuntimeException("Hostel not found with id: " + hostelId));
        return floorRepository.findByHostelAndFloorNo(hostel, floorNo)
                .orElseThrow(() -> new RuntimeException(
                        "Floor not found for hostel id: " + hostelId + " and floor no: " + floorNo));
    }
}
