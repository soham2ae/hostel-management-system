package com.hostel.service;

import com.hostel.entity.Bed;
import com.hostel.entity.Room;

import java.util.List;

/**
 * Service contract for managing {@link Bed} records.
 *
 * <p>Defines the business operations available for creating, updating,
 * and retrieving beds. Bed status is the source of truth from which
 * room-level availability is derived elsewhere in the system.</p>
 */
public interface BedService {

    /**
     * Creates a new bed belonging to a room.
     *
     * <p>Resolves the owning {@link Room} from {@code roomId} and
     * attaches it to the bed before persisting, so that callers
     * (typically controllers) need only supply the parent identifier
     * rather than a hydrated {@link Room} entity.</p>
     *
     * <p>The bed's status is always forced to
     * {@link com.hostel.enums.BedStatus#VACANT} on creation, regardless
     * of any status set on the incoming {@code bed} parameter. A newly
     * created bed must never begin life as occupied.</p>
     *
     * @param roomId the identifier of the room this bed belongs to
     * @param bed    the bed to create
     * @return the created bed, with its generated identifier populated
     */
    Bed createBed(Long roomId, Bed bed);

    /**
     * Updates an existing bed.
     *
     * @param bedId the identifier of the bed to update
     * @param bed   the updated bed details
     * @return the updated bed
     */
    Bed updateBed(Long bedId, Bed bed);

    /**
     * Finds a bed by its identifier.
     *
     * @param bedId the bed identifier
     * @return the matching bed
     */
    Bed findBedById(Long bedId);

    /**
     * Finds all beds belonging to a given room.
     *
     * @param roomId the room identifier
     * @return beds in the room
     */
    List<Bed> findBedsByRoom(Long roomId);

    /**
     * Finds all currently vacant beds.
     *
     * @return vacant beds
     */
    List<Bed> findVacantBeds();

    /**
     * Finds all currently occupied beds.
     *
     * @return occupied beds
     */
    List<Bed> findOccupiedBeds();
}
