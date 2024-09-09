package com.cm39.cm39.order.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class ApiResponse<T> {
    String result;
    T data;
    String message;
    String errorCode;
}
