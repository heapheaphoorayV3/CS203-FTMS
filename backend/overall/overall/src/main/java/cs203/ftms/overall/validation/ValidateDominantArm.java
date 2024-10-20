package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateDominantArm implements ConstraintValidator<ValidDominantArm , Character> {
    
    @Override
    public void initialize(ValidDominantArm annotation) {
    }

    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;  
        }
        return value.equals('L') || value.equals('R');
    }
}