package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating contact numbers.
 * <p>
 * This annotation ensures that the annotated field contains a valid contact number 
 * as determined by the {@link ValidateContactNumber} validator.
 * </p>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * &#64;ValidContactNumber
 * private String contactNumber;
 * </pre>
 */
@Constraint(validatedBy = ValidateContactNumber.class) // Specifies the validator class
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE }) // Annotation can be applied to fields, methods, parameters, and other annotations
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
public @interface ValidContactNumber {

    /**
     * Default validation message when the contact number is invalid.
     *
     * @return the default error message
     */
    String message() default "Invalid Contact Number";  

    /**
     * Defines groups for which this constraint can be applied.
     *
     * @return an array of class groups
     */
    Class<?>[] groups() default {}; 

    /**
     * Defines payloads that can be used by clients of the API to assign custom error details.
     *
     * @return an array of payload types
     */
    Class<? extends Payload>[] payload() default {};  
}
