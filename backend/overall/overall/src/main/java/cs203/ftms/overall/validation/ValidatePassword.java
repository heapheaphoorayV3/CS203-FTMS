package cs203.ftms.overall.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidatePassword implements ConstraintValidator<ValidPassword , String> {
    
    @Override
    public void initialize(ValidPassword annotation) {
    }

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
        for(int i=0;i < value.length();i++) {
            ch = value.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            } else if (symbols.indexOf(ch) != -1) {
                symbolFlag = true; 
            } else {return false;}
            if(numberFlag && capitalFlag && lowerCaseFlag && symbolFlag)
                return true;
        }
        return false; 
    }
}