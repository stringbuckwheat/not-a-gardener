package xyz.notagardener.common.error.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.AlreadyWateredException;


@Aspect
@Component
@Slf4j
public class ConstraintViolationAspect {
    @AfterThrowing(pointcut = "execution(* xyz.notagardener.watering..*(..))", throwing = "ex")
    public void handleWateringConstraintViolationException(ConstraintViolationException ex) {
        throw new AlreadyWateredException(ExceptionCode.ALREADY_WATERED);
    }
}
