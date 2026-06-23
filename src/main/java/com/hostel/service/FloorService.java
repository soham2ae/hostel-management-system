package com.hostel.service;

import com.hostel.entity.Floor;
import com.hostel.entity.Hostel;

import java.util.List;

/**
 * Service contract for managing {@link Floor} records.
 *
 * <p>Defines the business operations available for creating, updating,
 * and retrieving floors. Floor searches are preferentially scoped to a
 * specific {@link Hostel} to remain correct as multiple hostels are
 * onboarded.</p>
 */
public interface FloorService {

    /**
     * Creates a new floor belonging to a hostel.
     *
     * <p>Resolves the owning {@link Hostel} from {@code hostelId} and
     * attaches it to the floor before persisting, so that callers
     * (typically controllers) need only supply the parent identifier
     * rather than a hydrated {@link Hostel} entity.</p>
     *
     * @param hostelId the identifier of the hostel this floor belongs to
     * @param floor    the floor to create
     * @return the created floor, with its generated identifier populated
     */
    Floor createFloor(Long hostelId, Floor floor);

    /**
     * Updates an existing floor.
     *
     * @param floorId the identifier of the floor to update
     * @param floor   the updated floor details
     * @return the updated floor
     */
    Floor updateFloor(Long floorId, Floor floor);

    /**
     * Finds a floor by its identifier.
     *
     * @param floorId the floor identifier
     * @return the matching floor
     */
    Floor findFloorById(Long floorId);

    /**
     * Finds all floors belonging to a given hostel.
     *
     * @param hostelId the hostel identifier
     * @return floors belonging to the hostel
     */
    List<Floor> findFloorsByHostel(Long hostelId);

    /**
     * Finds a floor within a given hostel by floor number.
     *
     * <p>This is the preferred, hostel-scoped lookup and should be used
     * in favor of an unscoped floor-number search, since floor numbers
     * are not guaranteed unique across hostels.</p>
     *
     * @param hostelId the hostel identifier
     * @param floorNo  the floor number
     * @return the matching floor
     */
    Floor findFloorByHostelAndFloorNo(Long hostelId, Integer floorNo);
}
