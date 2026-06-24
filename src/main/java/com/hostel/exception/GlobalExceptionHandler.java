package com.hostel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centralized exception handler translating exceptions raised by the
 * service layer into consistent HTTP error responses.
 *
 * <p>Mapping:</p>
 * <ul>
 *   <li>{@link ResourceNotFoundException} → {@code 404 NOT_FOUND}</li>
 *   <li>{@link ValidationException} (including
 *       {@link InvalidAmountException}) → {@code 400 BAD_REQUEST}</li>
 *   <li>{@link BusinessRuleViolationException} (including
 *       {@link DuplicateBookingException},
 *       {@link DuplicateHostelException},
 *       {@link DuplicateUsernameException},
 *       {@link BedOccupiedException},
 *       {@link InsufficientWalletBalanceException}, and
 *       {@link InstallmentOwnershipException}) → {@code 409 CONFLICT}</li>
 *   <li>any other {@link HostelException} → {@code 500 INTERNAL_SERVER_ERROR}</li>
 *   <li>any unmapped {@link Exception} → {@code 500 INTERNAL_SERVER_ERROR}</li>
 * </ul>
 *
 * <p>The response body shape is defined inline here rather than as a
 * separate DTO class, since no DTO layer exists yet in this project.
 * This should be revisited once the DTO layer is introduced.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ResourceNotFoundException}, returning
     * {@code 404 NOT_FOUND}.
     *
     * @param ex      the exception
     * @param request the current web request
     * @return the error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex,
                                                                       WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles {@link ValidationException} and its subtypes (including
     * {@link InvalidAmountException}), returning
     * {@code 400 BAD_REQUEST}.
     *
     * @param ex      the exception
     * @param request the current web request
     * @return the error response
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException ex,
                                                                  WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles {@link BusinessRuleViolationException} and its subtypes
     * ({@link DuplicateBookingException},
     * {@link DuplicateHostelException},
     * {@link DuplicateUsernameException}, {@link BedOccupiedException},
     * {@link InsufficientWalletBalanceException}, and
     * {@link InstallmentOwnershipException}), returning
     * {@code 409 CONFLICT}.
     *
     * @param ex      the exception
     * @param request the current web request
     * @return the error response
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRuleViolation(BusinessRuleViolationException ex,
                                                                            WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles any {@link HostelException} not already matched by a
     * more specific handler above, returning
     * {@code 500 INTERNAL_SERVER_ERROR}.
     *
     * <p>This should rarely, if ever, be reached given the current
     * hierarchy, but guards against a future subclass being added
     * directly under {@link HostelException} without a dedicated
     * handler.</p>
     *
     * @param ex      the exception
     * @param request the current web request
     * @return the error response
     */
    @ExceptionHandler(HostelException.class)
    public ResponseEntity<Map<String, Object>> handleHostelException(HostelException ex,
                                                                       WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    /**
     * Handles any exception not covered above, returning
     * {@code 500 INTERNAL_SERVER_ERROR}.
     *
     * <p>This is a deliberate fallback for unexpected errors (e.g.
     * {@link NullPointerException}) and is intentionally separate from
     * {@link #handleHostelException}, which only covers known,
     * intentional failures raised by this system's own business
     * logic.</p>
     *
     * @param ex      the exception
     * @param request the current web request
     * @return the error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedException(Exception ex,
                                                                           WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    /**
     * Builds a consistent error response body.
     *
     * @param status  the HTTP status to return
     * @param message the error message
     * @param request the current web request
     * @return the error response
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status,
                                                                String message,
                                                                WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, status);
    }
}
