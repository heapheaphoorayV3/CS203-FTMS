package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for password complexity requirements.
 * <p>
 * Ensures that a password meets specific criteria: it must be between 8 and 20 characters
 * in length and contain at least one uppercase letter, one lowercase letter, one digit, and one symbol.
 * </p>
 */
public class ValidatePassword implements ConstraintValidator<ValidPassword, String> {
    
    /**
     * Initializes the validator. This implementation does not require any specific initialization.
     *
     * @param annotation The annotation instance for which this validator is being initialized.
     */
    @Override
    public void initialize(ValidPassword annotation) {
        // No initialization required for this validator.
    }

    /**
     * Checks if the provided password meets the complexity requirements:
     * <ul>
     *   <li>Length is between 8 and 20 characters</li>
     *   <li>Contains at least one uppercase letter</li>
     *   <li>Contains at least one lowercase letter</li>
     *   <li>Contains at least one digit</li>
     *   <li>Contains at least one symbol from the specified set</li>
     * </ul>
     *
     * @param value   The password to validate.
     * @param context Provides context data and operation when applying the validator.
     * @return {@code true} if the password meets all requirements; {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value.length() < 8 || value.length() > 20) return false;

        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean symbolFlag = false;
        String symbols = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

        for (int i = 0; i < value.length(); i++) {
            ch = value.charAt(i);
            if (Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            } else if (symbols.indexOf(ch) != -1) {
                symbolFlag = true;
            } else {
                return false;  // Invalid character not in the accepted set.
            }
            if (numberFlag && capitalFlag && lowerCaseFlag && symbolFlag) {
                return true;  // All requirements met.
            }
        }
        return false;
    }
}
