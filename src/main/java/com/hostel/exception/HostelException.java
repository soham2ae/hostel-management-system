package com.hostel.exception;

/**
 * Abstract root of all custom exceptions thrown by the Hostel ERP
 * System's service layer.
 *
 * <p>This class is never thrown directly. It exists so that
 * {@code GlobalExceptionHandler} (and any other caller) has a single
 * common type representing "a known, intentional failure raised by our
 * own business logic," distinct from unexpected runtime errors (e.g.
 * {@link NullPointerException}) that should not be treated the same
 * way.</p>
 *
 * <p>All subclasses are unchecked, extending {@link RuntimeException}
 * transitively through this class, consistent with Spring idioms that
 * avoid forcing every service method to declare a {@code throws}
 * clause.</p>
 */
public abstract class HostelException extends RuntimeException {

    /**
     * Constructs a new {@code HostelException} with the given message.
     *
     * @param message the detail message
     */
    protected HostelException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code HostelException} with the given message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    protected HostelException(String message, Throwable cause) {
        super(message, cause);
    }
}
