package xyz.notagardener.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import xyz.notagardener.common.validation.validator.MediumTypeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MediumTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MediumTypeConstraints {
    String message() default "식재 환경을 확인해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
