package com.hostel.exception;

/**
 * Thrown when an attempt is made to create a user with a username that
 * already exists.
 *
 * <p>{@code username} is a unique constraint on the {@code users}
 * table. This exception is part of the frozen exception hierarchy but
 * has no current call site: {@code UserService} intentionally exposes
 * only {@code findUserByUsername} today. User creation, password
 * handling, and authentication belong to a future security phase, at
 * which point a {@code createUser}-style operation would throw this
 * exception after a proactive uniqueness check, before persistence is
 * attempted. Maps to {@code 409 CONFLICT} in
 * {@code GlobalExceptionHandler}, inherited from
 * {@link BusinessRuleViolationException}.</p>
 */
public class DuplicateUsernameException extends BusinessRuleViolationException {

    /**
     * Constructs a new {@code DuplicateUsernameException} for the given
     * username.
     *
     * @param username the username that already exists
     */
    public DuplicateUsernameException(String username) {
        super("A user already exists with username: '" + username + "'");
    }
}
