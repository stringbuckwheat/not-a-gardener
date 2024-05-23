package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.MediumTypeConstraints;
import xyz.notagardener.plant.plant.dto.MediumType;

public class MediumTypeValidator implements ConstraintValidator<MediumTypeConstraints, String> {
    @Override
    public void initialize(MediumTypeConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return MediumType.isValid(value);
    }
}
