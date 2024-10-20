package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateWeapon implements ConstraintValidator<ValidWeapon , Character> {
    
    @Override
    public void initialize(ValidWeapon annotation) {
    }

    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;  
        }
        return value.equals('S') || value.equals('F')||value.equals('E');
    }
}
