package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for weapon type validation in fencing context.
 * <p>
 * Ensures that the weapon type is one of the valid codes: 'S' for Sabre, 'F' for Foil, or 'E' for Epee.
 * </p>
 */
public class ValidateWeapon implements ConstraintValidator<ValidWeapon, Character> {
    
    /**
     * Initializes the validator. This implementation does not require specific initialization.
     *
     * @param annotation The annotation instance for which this validator is being initialized.
     */
    @Override
    public void initialize(ValidWeapon annotation) {
        // No initialization required for this validator.
    }

    /**
     * Checks if the provided weapon code is valid.
     * <ul>
     *   <li>'S' - Represents Sabre</li>
     *   <li>'F' - Represents Foil</li>
     *   <li>'E' - Represents Epee</li>
     * </ul>
     *
     * @param value   The weapon code to validate.
     * @param context Provides context data and operations when applying the validator.
     * @return {@code true} if the weapon code is valid; {@code false} otherwise.
     */
    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.equals('S') || value.equals('F') || value.equals('E');
    }
}
