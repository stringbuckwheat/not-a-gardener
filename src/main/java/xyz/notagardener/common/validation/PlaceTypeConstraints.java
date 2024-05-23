package xyz.notagardener.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import xyz.notagardener.common.validation.validator.PlaceTypeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlaceTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PlaceTypeConstraints {
    String message() default "장소 분류를 확인해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
