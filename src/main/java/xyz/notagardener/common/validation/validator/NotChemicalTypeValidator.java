package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.chemical.dto.ChemicalType;
import xyz.notagardener.common.validation.ChemicalTypeConstraints;

public class NotChemicalTypeValidator implements ConstraintValidator<ChemicalTypeConstraints, String> {
    @Override
    public void initialize(ChemicalTypeConstraints constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ChemicalType.isValid(value);
    }
}
