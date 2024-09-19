package com.cm39.cm39.order.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"ordDate"})
public class OrderDto {
    private String ordNo;
    private String ordName;
    private String userId;
    private Integer totalOrdPrice;
    private LocalDateTime ordDate;
    private String ordStatCode;
}
