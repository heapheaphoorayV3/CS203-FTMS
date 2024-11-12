package cs203.ftms.overall.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


// Method to handle both MethodArgumentNotValidException and ConstraintViolationException
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        // List<String> errorMessages = new ArrayList<>();

        // Handle MethodArgumentNotValidException (for @Valid on @RequestBody)
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
            for (FieldError error : manve.getBindingResult().getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
                // errorMessages.add(error.getField() + ": " + error.getDefaultMessage());
            }
        }

        // Handle ConstraintViolationException (for @Valid on @PathVariable or @RequestParam)
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
                String[] fullfield = violation.getPropertyPath().toString().split("\\.");
                if ((fullfield.length) > 0) {
                    String field = fullfield[fullfield.length-1];
                    errors.put(field, violation.getMessage());
                } else {
                    errors.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
            }
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FencerProfileIncompleteException.class)
    public ResponseEntity<String> handleProfileIncomplete(FencerProfileIncompleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventAlreadyExistsException.class) 
    public ResponseEntity<String> handleEventAlreadyExists(EventAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityDoesNotExistException.class) 
    public ResponseEntity<String> handleEntityDoesNotExist(EntityDoesNotExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignUpDateOverExcpetion.class)
    public ResponseEntity<String> handleSignUpDateOver(SignUpDateOverExcpetion ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExist(UserAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignUpDateNotOverException.class)
    public ResponseEntity<String> handleSignUpDateNotOver(SignUpDateNotOverException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventCannotEndException.class)
    public ResponseEntity<String> handlesEventCannotEnd(EventCannotEndException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FencerAlreadyRegisteredForEventException.class)
    public ResponseEntity<String> handleFencerAlreadyRegisteredForEvent(FencerAlreadyRegisteredForEventException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TournamentAlreadyStartedException.class)
    public ResponseEntity<String> handleTournamentAlreadyStarted(TournamentAlreadyStartedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PouleMatchesNotDoneException.class)
    public ResponseEntity<String> handlePouleMatchesNotDone(PouleMatchesNotDoneException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FencerWeaponMismatchException.class)
    public ResponseEntity<String> handleFencerWeaponMismatch(FencerWeaponMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}


