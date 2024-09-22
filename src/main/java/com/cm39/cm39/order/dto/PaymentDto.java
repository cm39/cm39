package com.cm39.cm39.order.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaymentDto {
    private String payNo;
    private String ordNo;
    private String payKey;

    private String payCode;

    private String payStatCode;
    private Integer payPrice;

    private String dpsr;
    private String dpsrAccNo;
    private String dpsrBank;
}