package xyz.notagardener.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import xyz.notagardener.common.validation.validator.UsernameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraints {
    String message() default "아이디는 영문 소문자 혹은 숫자, 6자 이상 20자 이하여야해요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
