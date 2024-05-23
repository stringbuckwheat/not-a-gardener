package xyz.notagardener.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import xyz.notagardener.common.validation.validator.YesOrNoValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YesOrNoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface YesOrNo {
    String message() default "예/아니오로 대답해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
