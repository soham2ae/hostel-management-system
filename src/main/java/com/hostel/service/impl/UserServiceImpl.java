package com.hostel.service.impl;

import com.hostel.entity.User;
import com.hostel.repository.UserRepository;
import com.hostel.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UserService}.
 *
 * <p>Contains all business logic for retrieving {@link User} records.
 * Repository details are not exposed beyond this class.</p>
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserServiceImpl}.
     *
     * @param userRepository the user repository
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
