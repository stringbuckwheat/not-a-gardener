package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.YesOrNo;

public class YesOrNoValidator implements ConstraintValidator<YesOrNo, String> {
    @Override
    public void initialize(YesOrNo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ("Y".equals(value) || "N".equals(value));
    }
}
