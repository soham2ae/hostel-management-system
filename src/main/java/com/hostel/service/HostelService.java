package com.hostel.service;

import com.hostel.entity.Hostel;

import java.util.List;

/**
 * Service contract for managing {@link Hostel} records.
 *
 * <p>Defines the business operations available for creating, updating,
 * and retrieving hostels. Implementations are responsible for
 * validation, transaction management, and any orchestration required
 * across the data access layer.</p>
 */
public interface HostelService {

    /**
     * Creates a new hostel.
     *
     * @param hostel the hostel to create
     * @return the created hostel, with its generated identifier populated
     */
    Hostel createHostel(Hostel hostel);

    /**
     * Updates an existing hostel.
     *
     * @param hostelId the identifier of the hostel to update
     * @param hostel   the updated hostel details
     * @return the updated hostel
     */
    Hostel updateHostel(Long hostelId, Hostel hostel);

    /**
     * Finds a hostel by its identifier.
     *
     * @param hostelId the hostel identifier
     * @return the matching hostel
     */
    Hostel findHostelById(Long hostelId);

    /**
     * Finds a hostel by its unique hostel code.
     *
     * @param hostelCode the hostel code (e.g. "JHU")
     * @return the matching hostel
     */
    Hostel findHostelByCode(String hostelCode);

    /**
     * Lists all hostels.
     *
     * @return all hostels in the system
     */
    List<Hostel> findAllHostels();
}
