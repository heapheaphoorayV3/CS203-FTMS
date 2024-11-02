package cs203.ftms.tournament.controller;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidateDominantArm.class) // Link to the validator class
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDominantArm {

    String message() default "Dominant arm must be either L or R";  // Default validation message

    Class<?>[] groups() default {}; // Grouping constraints

    Class<? extends Payload>[] payload() default {};  // Payloads used by clients of the API
}