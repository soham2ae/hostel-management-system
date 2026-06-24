package com.hostel.service.impl;

import com.hostel.entity.Bed;
import com.hostel.entity.Floor;
import com.hostel.entity.Room;
import com.hostel.enums.BedStatus;
import com.hostel.exception.ResourceNotFoundException;
import com.hostel.repository.BedRepository;
import com.hostel.repository.FloorRepository;
import com.hostel.repository.RoomRepository;
import com.hostel.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link RoomService}.
 *
 * <p>Contains all business logic for creating, updating, and
 * retrieving {@link Room} records, including derivation of room
 * availability from live bed statuses. Repository details are not
 * exposed beyond this class.</p>
 */
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final BedRepository bedRepository;

    /**
     * Constructs a new {@code RoomServiceImpl}.
     *
     * @param roomRepository  the room repository
     * @param floorRepository the floor repository, used to resolve the
     *                        owning floor when creating a room
     * @param bedRepository   the bed repository, used to derive room
     *                        availability from live bed statuses
     */
    public RoomServiceImpl(RoomRepository roomRepository,
                            FloorRepository floorRepository,
                            BedRepository bedRepository) {
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.bedRepository = bedRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Room createRoom(Long floorId, Room room) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor", "id", floorId));
        room.setFloor(floor);
        return roomRepository.save(room);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Room updateRoom(Long roomId, Room room) {
        Room existingRoom = findRoomById(roomId);
        existingRoom.setRoomNo(room.getRoomNo());
        existingRoom.setRoomTypology(room.getRoomTypology());
        return roomRepository.save(existingRoom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Room> findRoomsByFloor(Long floorId) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor", "id", floorId));
        return roomRepository.findByFloor(floor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Room> findRoomsByRoomNo(String roomNo) {
        return roomRepository.findByRoomNo(roomNo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public String getRoomAvailabilityLabel(Long roomId) {
        Room room = findRoomById(roomId);
        List<Bed> beds = bedRepository.findByRoom(room);

        long totalBeds = beds.size();
        long vacantBeds = beds.stream()
                .filter(bed -> bed.getStatus() == BedStatus.VACANT)
                .count();

        if (vacantBeds == 0) {
            return "BOOKED";
        }
        return vacantBeds + "/" + totalBeds + " Beds Available";
    }
}
