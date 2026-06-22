package com.hostel.repository;

import com.hostel.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data access repository for {@link Hostel}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 */
public interface HostelRepository extends JpaRepository<Hostel, Long> {

    /**
     * Finds a hostel by its unique hostel code.
     *
     * @param hostelCode the hostel code (e.g. "JHU")
     * @return the matching hostel, if any
     */
    Optional<Hostel> findByHostelCode(String hostelCode);

    /**
     * Finds a hostel by its unique hostel name.
     *
     * @param hostelName the hostel name (e.g. "Juhu")
     * @return the matching hostel, if any
     */
    Optional<Hostel> findByHostelName(String hostelName);
}
