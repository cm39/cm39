package com.cm39.cm39.validator;

import jakarta.validation.Payload;

// custom annotation : 현재보다 과거의 날짜인가
public @interface NotFutureDate {
    String message() default "유효하지 않은 날짜입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
