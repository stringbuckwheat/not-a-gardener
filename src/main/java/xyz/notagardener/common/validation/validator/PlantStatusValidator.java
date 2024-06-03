package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.PlantStatusConstraints;
import xyz.notagardener.status.dto.PlantStatusType;

public class PlantStatusValidator implements ConstraintValidator<PlantStatusConstraints, String> {
    @Override
    public void initialize(PlantStatusConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || PlantStatusType.isValid(value);
    }
}
