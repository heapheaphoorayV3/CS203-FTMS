package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating the dominant arm field of an entity.
 * <p>
 * This annotation ensures that the annotated field contains a valid value representing the dominant arm, 
 * with allowed values being 'L' for left and 'R' for right.
 * </p>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * &#64;ValidDominantArm
 * private char dominantArm;
 * </pre>
 */
@Constraint(validatedBy = ValidateDominantArm.class) // Specifies the validator class for dominant arm validation
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE }) // Applicable to fields, methods, parameters, and other annotations
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
public @interface ValidDominantArm {

    /**
     * Default validation message when the dominant arm value is invalid.
     *
     * @return the default error message
     */
    String message() default "Dominant arm must be either L or R";  

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
