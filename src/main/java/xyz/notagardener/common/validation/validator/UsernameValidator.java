package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.UsernameConstraints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<UsernameConstraints, String> {
        private static final String USERNAME_REGEX = "^[A-Za-z]{1}[A-Za-z0-9_-]{5,19}$";

    @Override
    public void initialize(UsernameConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(USERNAME_REGEX);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
