package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating gender input in entities.
 * <p>
 * This annotation ensures that the annotated field has a valid gender value, where only 'M' (Male) 
 * and 'W' (Woman) are acceptable values.
 * </p>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * &#64;ValidGender
 * private char gender;
 * </pre>
 */
@Constraint(validatedBy = ValidateGender.class) // Specifies the validator class for gender validation
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE }) // Applicable to fields, methods, parameters, and other annotations
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
public @interface ValidGender {

    /**
     * Default validation message when an invalid gender is provided.
     *
     * @return the error message
     */
    String message() default "Gender must be either M or W";  

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
