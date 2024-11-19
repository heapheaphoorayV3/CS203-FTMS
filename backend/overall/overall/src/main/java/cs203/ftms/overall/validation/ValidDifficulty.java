package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating the difficulty level of an entity.
 * <p>
 * This annotation ensures that the annotated field contains one of the allowed difficulty values: 'B', 'I', or 'A', 
 * which represent Beginner, Intermediate, and Advanced levels, respectively.
 * </p>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * &#64;ValidDifficulty
 * private char difficulty;
 * </pre>
 */
@Constraint(validatedBy = ValidateDifficulty.class) // Specifies the validator class
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE }) // Annotation can be applied to fields, methods, parameters, and other annotations
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
public @interface ValidDifficulty {

    /**
     * Default validation message when the difficulty level is invalid.
     *
     * @return the default error message
     */
    String message() default "Difficulty must be either B, I, or A";  

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
