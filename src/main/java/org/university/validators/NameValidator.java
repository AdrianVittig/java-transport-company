package org.university.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidateNames, String> {

    private int min;
    private int max;


    @Override
    public void initialize(ValidateNames constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s ==null) return true;

        String name = s.trim();

        if(name.isBlank()) return false;

        if(name.length() < min || name.length() > max) return false;

        return true;
    }
}
