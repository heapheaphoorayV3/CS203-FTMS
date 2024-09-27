package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateName implements ConstraintValidator<ValidName , String> {
    
    private static final String NAME_PATTERN = "^[A-Za-z\\s'-]+$";

    @Override
    public void initialize(ValidName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String nameField, ConstraintValidatorContext context) {
        if (nameField == null || nameField.isEmpty()) {
            return false; 
        }

        return nameField.matches(NAME_PATTERN);
    }
}