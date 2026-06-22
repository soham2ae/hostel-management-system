package com.hostel.repository;

import com.hostel.entity.Floor;
import com.hostel.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Data access repository for {@link Floor}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 */
public interface FloorRepository extends JpaRepository<Floor, Long> {

    /**
     * Finds all floors belonging to a given hostel.
     *
     * @param hostel the hostel
     * @return floors belonging to the hostel
     */
    List<Floor> findByHostel(Hostel hostel);

    /**
     * Finds a floor by its floor number.
     *
     * <p>Floor numbers are not declared globally unique in the schema;
     * however, in practice a given floor number is expected to be
     * unique within a hostel. Callers needing floor number scoped to a
     * specific hostel should use {@link #findByHostelAndFloorNo}.</p>
     *
     * @param floorNo the floor number
     * @return the matching floor, if any
     */
    Optional<Floor> findByFloorNo(Integer floorNo);

    /**
     * Finds a floor by hostel and floor number.
     *
     * @param hostel  the hostel
     * @param floorNo the floor number
     * @return the matching floor, if any
     */
    Optional<Floor> findByHostelAndFloorNo(Hostel hostel, Integer floorNo);
}
