package xyz.notagardener.common.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import xyz.notagardener.common.validation.NotFuture;

import java.time.LocalDate;

public class NotFutureValidator implements ConstraintValidator<NotFuture, LocalDate> {
    @Override
    public void initialize(NotFuture constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date != null && !date.isAfter(LocalDate.now());
    }
}
