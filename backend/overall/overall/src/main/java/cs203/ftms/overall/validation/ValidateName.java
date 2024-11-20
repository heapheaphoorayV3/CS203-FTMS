package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator class for validating a person's name based on a predefined pattern.
 * The name can contain alphabetic characters, spaces, hyphens, and apostrophes.
 */
public class ValidateName implements ConstraintValidator<ValidName, String> {
    
    private static final String NAME_PATTERN = "^[A-Za-z\\s'-]+$";

    /**
     * Initializes the validator. This implementation does not require any specific initialization.
     *
     * @param constraintAnnotation The annotation instance for which this validator is being initialized.
     */
    @Override
    public void initialize(ValidName constraintAnnotation) {
        // No initialization needed for this validator.
    }

    /**
     * Validates that the provided name matches the specified pattern.
     * 
     * @param nameField The name string to validate.
     * @param context   Provides contextual data and operation when applying a given constraint validator.
     * @return {@code true} if the name is valid according to the specified pattern, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String nameField, ConstraintValidatorContext context) {
        if (nameField == null || nameField.isEmpty()) {
            return false; 
        }

        return nameField.matches(NAME_PATTERN);
    }
}
