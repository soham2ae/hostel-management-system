package com.hostel.service.impl;

import com.hostel.entity.Hostel;
import com.hostel.repository.HostelRepository;
import com.hostel.service.HostelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link HostelService}.
 *
 * <p>Contains all business logic for creating, updating, and
 * retrieving {@link Hostel} records. Repository details are not
 * exposed beyond this class.</p>
 */
@Service
public class HostelServiceImpl implements HostelService {

    private final HostelRepository hostelRepository;

    /**
     * Constructs a new {@code HostelServiceImpl}.
     *
     * @param hostelRepository the hostel repository
     */
    public HostelServiceImpl(HostelRepository hostelRepository) {
        this.hostelRepository = hostelRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Hostel createHostel(Hostel hostel) {
        return hostelRepository.save(hostel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Hostel updateHostel(Long hostelId, Hostel hostel) {
        Hostel existingHostel = findHostelById(hostelId);
        existingHostel.setHostelName(hostel.getHostelName());
        existingHostel.setHostelCode(hostel.getHostelCode());
        return hostelRepository.save(existingHostel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Hostel findHostelById(Long hostelId) {
        return hostelRepository.findById(hostelId)
                .orElseThrow(() -> new RuntimeException("Hostel not found with id: " + hostelId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Hostel findHostelByCode(String hostelCode) {
        return hostelRepository.findByHostelCode(hostelCode)
                .orElseThrow(() -> new RuntimeException("Hostel not found with code: " + hostelCode));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Hostel> findAllHostels() {
        return hostelRepository.findAll();
    }
}
