package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for validating the difficulty level character field.
 * Implements the {@link ConstraintValidator} interface to check if the provided value is a valid difficulty level.
 * Expected values are 'B' for Beginner, 'I' for Intermediate, and 'A' for Advanced.
 */
public class ValidateDifficulty implements ConstraintValidator<ValidDifficulty, Character> {
    
    /**
     * Initializes the validator, typically used to fetch annotation parameters if needed.
     * In this case, no specific initialization is required.
     *
     * @param annotation the constraint annotation.
     */
    @Override
    public void initialize(ValidDifficulty annotation) {
    }

    /**
     * Validates the difficulty level to ensure it matches one of the accepted values: 'B', 'I', or 'A'.
     *
     * @param value   the difficulty character.
     * @param context context in which the constraint is evaluated.
     * @return {@code true} if the difficulty is one of 'B', 'I', or 'A'; {@code false} otherwise.
     */
    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;  
        }
        return value.equals('B') || value.equals('I') || value.equals('A');
    }
}
