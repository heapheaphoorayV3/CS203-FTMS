package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating the type of weapon in a fencing context.
 * <p>
 * This annotation enforces a constraint where the weapon must be one of the following values:
 * <ul>
 *   <li>'S' for Sabre</li>
 *   <li>'E' for Épée</li>
 *   <li>'F' for Foil</li>
 * </ul>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * &#64;ValidWeapon
 * private char weapon;
 * </pre>
 */
@Constraint(validatedBy = ValidateWeapon.class) // Specifies the class responsible for validation logic
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE }) // Applicable to fields, methods, parameters, and annotations
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
public @interface ValidWeapon {

    /**
     * Default error message when a weapon type does not match the required values.
     *
     * @return the error message
     */
    String message() default "Weapon must be either S, E or F";

    /**
     * Specifies groups for grouping constraints.
     *
     * @return an array of class groups
     */
    Class<?>[] groups() default {}; 

    /**
     * Allows attaching custom payload objects to the constraint.
     *
     * @return an array of payload types
     */
    Class<? extends Payload>[] payload() default {};  
}
