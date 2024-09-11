package com.cm39.cm39.order.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    static final int MAX_QTY = 100;
    static final int MIN_QTY = 1;

    private Integer cartSeq;            // 장바구니 번호
    private String userId;              // 유저 아이디
    private String  prodNo;             // 상품 번호

    @NotBlank
    private String itemNo;              // 품목 번호

    @NotNull
    @Min(value = MIN_QTY, message = "1 이하는 선택 불가능 합니다.")
    private Integer qty;                // 상품 수량
}
