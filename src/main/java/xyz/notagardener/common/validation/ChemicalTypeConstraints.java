package xyz.notagardener.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import xyz.notagardener.common.validation.validator.NotChemicalTypeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotChemicalTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChemicalTypeConstraints {
    String message() default "약품 타입을 확인해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
