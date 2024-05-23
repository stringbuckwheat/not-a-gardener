package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.PasswordConstraints;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordConstraints, String> {
    private static final String PASSWORD_REGEX =
            "(?=.*\\d{1,50})(?=.*[~`!@#$%\\^&*()\\-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$";

    @Override
    public void initialize(PasswordConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}
