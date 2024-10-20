package cs203.ftms.overall.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateContactNumber implements ConstraintValidator<ValidContactNumber , String> {
    
    @Override
    public void initialize(ValidContactNumber annotation) {
    }

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