package xyz.notagardener.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import xyz.notagardener.common.validation.validator.PasswordValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraints {
    String message() default "비밀번호는 숫자, 특수문자를 포함하여 8자리 이상이어야 해요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
