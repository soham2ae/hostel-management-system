package com.hostel.exception;

/**
 * Thrown when a requested resource does not exist.
 *
 * <p>This is the single exception type used across every service for
 * "not found" lookups — by identifier, by a unique field, or by a
 * required relationship that does not currently hold. Maps to
 * {@code 404 NOT_FOUND} in {@code GlobalExceptionHandler}.</p>
 */
public class ResourceNotFoundException extends HostelException {

    /**
     * Constructs a new {@code ResourceNotFoundException} with a
     * pre-built message.
     *
     * <p>Use this constructor when the standard
     * {@code "<resource> not found with <field>: '<value>'"} phrasing
     * does not fit (e.g. "No student currently occupies bed id: 5").</p>
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ResourceNotFoundException} describing
     * which resource was not found, by which field, and what value was
     * searched for.
     *
     * <p>Produces a message of the form
     * {@code "<resourceName> not found with <fieldName>: '<fieldValue>'"}.</p>
     *
     * @param resourceName the name of the resource type (e.g. "Student")
     * @param fieldName    the name of the field that was searched
     *                     (e.g. "id", "booking number")
     * @param fieldValue   the value that was searched for
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName + " not found with " + fieldName + ": '" + fieldValue + "'");
    }
}
