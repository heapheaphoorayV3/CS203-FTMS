package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for validating the dominant arm character field.
 * Implements the {@link ConstraintValidator} interface to check if the provided value is a valid dominant arm identifier.
 * Expected values are 'L' for Left and 'R' for Right.
 */
public class ValidateDominantArm implements ConstraintValidator<ValidDominantArm, Character> {
    
    /**
     * Initializes the validator, typically used to fetch annotation parameters if needed.
     * In this case, no specific initialization is required.
     *
     * @param annotation the constraint annotation.
     */
    @Override
    public void initialize(ValidDominantArm annotation) {
    }

    /**
     * Validates the dominant arm identifier to ensure it matches one of the accepted values: 'L' or 'R'.
     *
     * @param value   the dominant arm character.
     * @param context context in which the constraint is evaluated.
     * @return {@code true} if the dominant arm is either 'L' or 'R'; {@code false} otherwise.
     */
    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;  
        }
        return value.equals('L') || value.equals('R');
    }
}
