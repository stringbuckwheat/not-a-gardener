package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.YesOrNo;
import xyz.notagardener.common.validation.YesOrNoType;

public class YesOrNoValidator implements ConstraintValidator<YesOrNo, YesOrNoType> {
    @Override
    public void initialize(YesOrNo constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(YesOrNoType value, ConstraintValidatorContext context) {
        return YesOrNoType.isValid(value);
    }
}
