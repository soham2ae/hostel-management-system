package com.hostel.exception;

import java.math.BigDecimal;

/**
 * Thrown when a monetary amount supplied to a service operation is not
 * strictly positive.
 *
 * <p>Used wherever a monetary input must be greater than zero — for
 * example, wallet top-up and debit amounts. Rejects {@code null},
 * zero, and negative values alike. Maps to {@code 400 BAD_REQUEST} in
 * {@code GlobalExceptionHandler}, inherited from
 * {@link ValidationException}.</p>
 */
public class InvalidAmountException extends ValidationException {

    /**
     * Constructs a new {@code InvalidAmountException} with a pre-built
     * message.
     *
     * @param message the detail message
     */
    public InvalidAmountException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidAmountException} describing which
     * field failed validation and what value was supplied.
     *
     * <p>Produces a message of the form
     * {@code "<fieldName> must be a positive amount, but was: <amount>"}.</p>
     *
     * @param fieldName the name of the monetary field that failed
     *                  validation (e.g. "creditAmount")
     * @param amount    the value that was supplied, possibly {@code null}
     */
    public InvalidAmountException(String fieldName, BigDecimal amount) {
        super(fieldName + " must be a positive amount, but was: " + amount);
    }
}
