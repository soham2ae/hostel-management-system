package com.hostel.service;

import com.hostel.entity.Floor;
import com.hostel.entity.Room;

import java.util.List;

/**
 * Service contract for managing {@link Room} records.
 *
 * <p>Defines the business operations available for creating, updating,
 * and retrieving rooms. Room availability is never stored — it is
 * always derived from the live statuses of a room's
 * {@link com.hostel.entity.Bed} records.</p>
 */
public interface RoomService {

    /**
     * Creates a new room belonging to a floor.
     *
     * <p>Resolves the owning {@link Floor} from {@code floorId} and
     * attaches it to the room before persisting, so that callers
     * (typically controllers) need only supply the parent identifier
     * rather than a hydrated {@link Floor} entity.</p>
     *
     * @param floorId the identifier of the floor this room belongs to
     * @param room    the room to create
     * @return the created room, with its generated identifier populated
     */
    Room createRoom(Long floorId, Room room);

    /**
     * Updates an existing room.
     *
     * @param roomId the identifier of the room to update
     * @param room   the updated room details
     * @return the updated room
     */
    Room updateRoom(Long roomId, Room room);

    /**
     * Finds a room by its identifier.
     *
     * @param roomId the room identifier
     * @return the matching room
     */
    Room findRoomById(Long roomId);

    /**
     * Finds all rooms belonging to a given floor.
     *
     * @param floorId the floor identifier
     * @return rooms on the floor
     */
    List<Room> findRoomsByFloor(Long floorId);

    /**
     * Finds rooms by room number.
     *
     * <p>Room numbers are not globally unique, so this may return
     * multiple rooms across different floors or hostels.</p>
     *
     * @param roomNo the room number
     * @return rooms matching the given room number
     */
    List<Room> findRoomsByRoomNo(String roomNo);

    /**
     * Computes a human-readable availability label for a room, derived
     * dynamically from its current bed statuses.
     *
     * <p>Returns a label such as {@code "2/3 Beds Available"} when at
     * least one bed is vacant, or {@code "BOOKED"} when no beds remain
     * vacant. This value is never persisted.</p>
     *
     * @param roomId the room identifier
     * @return the derived availability label
     */
    String getRoomAvailabilityLabel(Long roomId);
}
