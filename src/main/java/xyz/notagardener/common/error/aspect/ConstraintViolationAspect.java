package xyz.notagardener.common.error.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.AlreadyRecordedStatusException;
import xyz.notagardener.common.error.exception.AlreadyRepottedException;
import xyz.notagardener.common.error.exception.AlreadyWateredException;


@Aspect
@Component
@Slf4j
public class ConstraintViolationAspect {
    @AfterThrowing(pointcut = "execution(* xyz.notagardener.watering..*(..))", throwing = "ex")
    public void handleWateringConstraintViolationException(ConstraintViolationException ex) {
        throw new AlreadyWateredException(ExceptionCode.ALREADY_WATERED);
    }

    @AfterThrowing(pointcut = "execution(* xyz.notagardener.repot..*(..))", throwing = "ex")
    public void handleRepotConstraintViolationException(DataIntegrityViolationException ex) {
        throw new AlreadyRepottedException(ExceptionCode.ALREADY_REPOTTED);
    }

    @AfterThrowing(pointcut = "execution(* xyz.notagardener.status..*(..))", throwing = "ex")
    public void handleStatusConstraintViolationException(DataIntegrityViolationException ex) {
        throw new AlreadyRecordedStatusException(ExceptionCode.ALREADY_RECORDED_STATUS);
    }
}
