package com.cm39.cm39.order.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHandlerFailResponse {
    private String code;
    private String message;
}
