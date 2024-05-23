package xyz.notagardener.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import xyz.notagardener.common.validation.validator.NotFutureValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotFutureValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFuture {
    String message() default "미래 날짜를 입력할 수 없어요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


