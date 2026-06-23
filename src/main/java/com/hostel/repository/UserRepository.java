package com.hostel.repository;

import com.hostel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data access repository for {@link User}.
 *
 * <p>Contains data-access logic only; no business logic.</p>
 *
 * <p>{@link #findByUsername} will later be used by a Spring Security
 * {@code UserDetailsService} implementation to load users during
 * authentication.</p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique username.
     *
     * @param username the username
     * @return the matching user, if any
     */
    Optional<User> findByUsername(String username);
}
