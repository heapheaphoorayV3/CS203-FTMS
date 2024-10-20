package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateGender implements ConstraintValidator<ValidGender , Character> {
    
    @Override
    public void initialize(ValidGender annotation) {
        // No initialization needed for this validator, but you can add custom logic here if needed
    }

    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {
        // The valid values for gender are "M" and "F"
        if (value == null) {
            return false;  // Gender must not be null
        }
        return value.equals('M') || value.equals('F');
    }
}
