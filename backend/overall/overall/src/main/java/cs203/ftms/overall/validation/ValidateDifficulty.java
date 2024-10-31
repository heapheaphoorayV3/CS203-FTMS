package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateDifficulty implements ConstraintValidator<ValidDifficulty , Character> {
    
    @Override
    public void initialize(ValidDifficulty annotation) {
    }

    @Override
    public boolean isValid(Character value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;  
        }
        return value.equals('B') || value.equals('I')||value.equals('A');
    }
}
