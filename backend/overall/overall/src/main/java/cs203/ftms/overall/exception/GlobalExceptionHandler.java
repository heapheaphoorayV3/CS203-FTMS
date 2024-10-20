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

}


