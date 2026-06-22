package com.hostel.repository;

import com.hostel.entity.Floor;
import com.hostel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access repository for {@link Room}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>Room availability is derived from {@link com.hostel.entity.Bed}
 * statuses and is never stored or queried here.</p>
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Finds all rooms belonging to a given floor.
     *
     * @param floor the floor
     * @return rooms on the floor
     */
    List<Room> findByFloor(Floor floor);

    /**
     * Finds rooms by room number.
     *
     * <p>Room numbers are not globally unique — the same room number
     * may exist on different floors or hostels — so this returns all
     * matches rather than a single result.</p>
     *
     * @param roomNo the room number
     * @return rooms matching the given room number
     */
    List<Room> findByRoomNo(String roomNo);
}
