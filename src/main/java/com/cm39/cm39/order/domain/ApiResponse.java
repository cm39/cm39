package com.cm39.cm39.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ApiResponse<T> {
    String result;
    T data;
    String message;
    String errorCode;
}
