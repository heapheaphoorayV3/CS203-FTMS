package cs203.ftms.overall.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for validating a contact number field using Google’s libphonenumber library.
 * Implements the {@link ConstraintValidator} interface to check if the contact number format is valid.
 */
public class ValidateContactNumber implements ConstraintValidator<ValidContactNumber, String> {
    
    /**
     * Initializes the validator, typically used to fetch annotation parameters if needed.
     * In this case, no specific initialization is required.
     *
     * @param annotation the constraint annotation.
     */
    @Override
    public void initialize(ValidContactNumber annotation) {
    }

    /**
     * Validates a contact number to ensure it follows the correct international format.
     *
     * @param contactField the contact number as a string.
     * @param context      context in which the constraint is evaluated.
     * @return {@code true} if the contact number is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext context) {
        if (contactField == null || contactField.isEmpty()) {
            return false;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(contactField, null);
            return phoneNumberUtil.isValidNumber(phoneNumber);  
        } catch (NumberParseException e) {
            return false;
        }
    }
}
