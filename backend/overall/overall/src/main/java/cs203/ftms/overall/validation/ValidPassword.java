package cs203.ftms.overall.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidatePassword.class) // Link to the validator class
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Password must be within 8 to 20 characters, and contain lowercase and uppercase characters, numbers and special characters!";  // Default validation message

    Class<?>[] groups() default {}; // Grouping constraints

    Class<? extends Payload>[] payload() default {};  // Payloads used by clients of the API
}