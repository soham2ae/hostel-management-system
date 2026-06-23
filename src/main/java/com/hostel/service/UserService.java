package com.hostel.service;

import com.hostel.entity.User;

/**
 * Service contract for managing {@link User} records.
 *
 * <p>{@link #findUserByUsername} will later be used by a Spring
 * Security {@code UserDetailsService} implementation to load users
 * during authentication.</p>
 */
public interface UserService {

    /**
     * Finds a user by their unique username.
     *
     * @param username the username
     * @return the matching user
     */
    User findUserByUsername(String username);
}
