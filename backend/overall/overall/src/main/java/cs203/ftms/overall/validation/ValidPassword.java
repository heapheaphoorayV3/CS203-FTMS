package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating password complexity requirements.
 * <p>
 * This annotation enforces a password policy that requires passwords to have:
 * </p>
 * <ul>
 *   <li>A length between 8 and 20 characters</li>
 *   <li>At least one uppercase letter</li>
 *   <li>At least one lowercase letter</li>
 *   <li>At least one digit</li>
 *   <li>At least one special character</li>
 * </ul>
 * <p>
 * Usage:
 * </p>
 * <pre>
 * &#64;ValidPassword
 * private String password;
 * </pre>
 */
@Constraint(validatedBy = ValidatePassword.class) // Specifies the class responsible for validation logic
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE }) // Applicable to fields, methods, parameters, and annotations
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
public @interface ValidPassword {

    /**
     * Default error message when a password does not meet complexity requirements.
     *
     * @return the error message
     */
    String message() default "Password must be within 8 to 20 characters, and contain lowercase and uppercase characters, numbers, and special characters!";

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
