package com.cm39.cm39.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartProductOptionDto {
    private String userId;          // 유저 아이디
    private Integer cartSeq;        // 장바구니 번호
    private String  optTypeNo;      // 옵션 타입 번호
    private Integer optDetailSeq;   // 옵션 상세 번호
    private String optDetailName;   // 옵션 상세명
}
