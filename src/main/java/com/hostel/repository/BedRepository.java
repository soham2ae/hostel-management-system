package com.hostel.repository;

import com.hostel.entity.Bed;
import com.hostel.entity.Room;
import com.hostel.enums.BedStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access repository for {@link Bed}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>This is the source of truth from which room-level availability
 * must be derived by the service layer; no derived availability value
 * is computed or stored here.</p>
 */
public interface BedRepository extends JpaRepository<Bed, Long> {

    /**
     * Finds all beds belonging to a given room.
     *
     * @param room the room
     * @return beds in the room
     */
    List<Bed> findByRoom(Room room);

    /**
     * Finds all beds with the given status.
     *
     * @param status the bed status
     * @return beds with the given status
     */
    List<Bed> findByStatus(BedStatus status);

    /**
     * Finds all vacant beds.
     *
     * @return beds currently vacant
     */
    default List<Bed> findVacantBeds() {
        return findByStatus(BedStatus.VACANT);
    }

    /**
     * Finds all occupied beds.
     *
     * @return beds currently occupied
     */
    default List<Bed> findOccupiedBeds() {
        return findByStatus(BedStatus.OCCUPIED);
    }
}
