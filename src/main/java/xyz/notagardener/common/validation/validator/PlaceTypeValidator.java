package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.PlaceTypeConstraints;
import xyz.notagardener.place.dto.PlaceType;

public class PlaceTypeValidator implements ConstraintValidator<PlaceTypeConstraints, String> {
    @Override
    public void initialize(PlaceTypeConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return PlaceType.isValid(value);
    }
}
