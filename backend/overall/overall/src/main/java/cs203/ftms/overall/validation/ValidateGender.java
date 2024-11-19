package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for validating the gender character field.
 * Implements the {@link ConstraintValidator} interface to check if the provided value is a valid gender identifier.
 * Expected values are 'M' for Male and 'W' for Woman.
 */
public class ValidateGender implements ConstraintValidator<ValidGender, Character> {

    /**
     * Initializes the validator. This method can include custom initialization logic if needed.
     * For this validator, no specific initialization is required.
     *
     * @param annotation the constraint annotation.
     */
    @Override
    public void initialize(ValidGender annotation) {
        // No initialization needed
    }

    /**
     * Validates the gender identifier to ensure it matches one of the accepted values: 'M' or 'W'.
     *
     * @param value   the gender character.
     * @param context context in which the constraint is evaluated.
     * @return {@code true} if the gender is either 'M' or 'W'; {@code false} if null or other values.
     */
    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;  // Gender must not be null
        }
        return value.equals('M') || value.equals('W');
    }
}
