package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.PlantStatusConstraints;
import xyz.notagardener.status.model.StatusType;

public class PlantStatusValidator implements ConstraintValidator<PlantStatusConstraints, StatusType> {
    @Override
    public void initialize(PlantStatusConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(StatusType value, ConstraintValidatorContext context) {
        return value == null || StatusType.isValid(value);
    }
}
