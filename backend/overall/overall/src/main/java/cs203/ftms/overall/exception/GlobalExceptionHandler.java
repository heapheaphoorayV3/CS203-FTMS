package cs203.ftms.overall.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for handling common exceptions across controllers.
 * Provides custom responses for validation and application-specific exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions for invalid method arguments and constraint violations.
     *
     * @param ex The exception thrown, either MethodArgumentNotValidException or ConstraintViolationException.
     * @return ResponseEntity containing a map of field errors and their messages, with HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
            for (FieldError error : manve.getBindingResult().getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }

        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                String[] fullfield = violation.getPropertyPath().toString().split("\\.");
                if (fullfield.length > 0) {
                    String field = fullfield[fullfield.length - 1];
                    errors.put(field, violation.getMessage());
                } else {
                    errors.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
            }
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles EntityNotFoundException when an entity is not found.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles FencerProfileIncompleteException when a fencer's profile is incomplete.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.NOT_FOUND.
     */
    @ExceptionHandler(FencerProfileIncompleteException.class)
    public ResponseEntity<String> handleProfileIncomplete(FencerProfileIncompleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles EventAlreadyExistsException when an event already exists.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(EventAlreadyExistsException.class)
    public ResponseEntity<String> handleEventAlreadyExists(EventAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles EntityDoesNotExistException when an entity does not exist.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<String> handleEntityDoesNotExist(EntityDoesNotExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles SignUpDateOverException when the sign-up date is over.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(SignUpDateOverException.class)
    public ResponseEntity<String> handleSignUpDateOver(SignUpDateOverException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UserAlreadyExistException when a user already exists.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExist(UserAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles SignUpDateNotOverException when the sign-up date is not over.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(SignUpDateNotOverException.class)
    public ResponseEntity<String> handleSignUpDateNotOver(SignUpDateNotOverException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles EventCannotEndException when an event cannot be ended.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(EventCannotEndException.class)
    public ResponseEntity<String> handlesEventCannotEnd(EventCannotEndException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles FencerAlreadyRegisteredForEventException when a fencer is already registered for an event.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(FencerAlreadyRegisteredForEventException.class)
    public ResponseEntity<String> handleFencerAlreadyRegisteredForEvent(FencerAlreadyRegisteredForEventException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles TournamentAlreadyStartedException when a tournament has already started.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(TournamentAlreadyStartedException.class)
    public ResponseEntity<String> handleTournamentAlreadyStarted(TournamentAlreadyStartedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles PouleMatchesNotDoneException when poule matches are not completed.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(PouleMatchesNotDoneException.class)
    public ResponseEntity<String> handlePouleMatchesNotDone(PouleMatchesNotDoneException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles FencerProfileMismatchException when there is a mismatch in the fencer's profile.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(FencerProfileMismatchException.class)
    public ResponseEntity<String> handleFencerWeaponMismatch(FencerProfileMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException when an invalid argument is passed.
     *
     * @param ex The exception thrown.
     * @return ResponseEntity with the exception message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
