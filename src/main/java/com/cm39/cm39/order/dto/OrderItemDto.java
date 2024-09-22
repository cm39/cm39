package com.cm39.cm39.order.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderItemDto {
    private String ordNo;
    private Integer ordItemSeq;

    private String prodNo;
    private String itemNo;

    private Integer itemQty;
    private Integer itemPrice;

    private String ordItemStatCode;
}