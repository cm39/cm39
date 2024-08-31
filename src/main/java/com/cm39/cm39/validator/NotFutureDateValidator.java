package com.cm39.cm39.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 검증 : 현재보다 과거의 날짜인가
public class NotFutureDateValidator implements ConstraintValidator<NotFutureDate, LocalDateTime> {

    @Override
    public void initialize(NotFutureDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 미검증 (다른 어노테이션으로 처리)
        }

        try {
            // 과거의 날짜일 경우 true 반환
            return value.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }
}
