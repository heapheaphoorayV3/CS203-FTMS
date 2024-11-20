package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating name fields in entities.
 * <p>
 * This annotation ensures that the annotated field only contains valid characters for a name,
 * typically letters, spaces, apostrophes, or hyphens.
 * </p>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * &#64;ValidName
 * private String name;
 * </pre>
 */
@Constraint(validatedBy = ValidateName.class) // Specifies the validator class for name validation
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER }) // Applicable to fields, methods, and parameters
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
public @interface ValidName {

    /**
     * Default validation message when an invalid name is provided.
     *
     * @return the error message
     */
    String message() default "Invalid name"; 

    /**
     * Specifies groups for constraint categorization.
     *
     * @return an array of class groups
     */
    Class<?>[] groups() default {}; 

    /**
     * Payloads for clients to add custom error details.
     *
     * @return an array of payload types
     */
    Class<? extends Payload>[] payload() default {};  
}
