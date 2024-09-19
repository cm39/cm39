package com.cm39.cm39.order.dto;

import com.cm39.cm39.order.vo.OrderFormItemVo;
import com.cm39.cm39.promotion.domain.CartCoupon;
import com.cm39.cm39.promotion.domain.ItemCoupon;
import com.cm39.cm39.user.domain.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFormResponse {
    private UserDto user;                               // 주문자 정보
    private Integer availableMileage;                   // 가용 적립금
    private List<OrderFormItemVo> orderFormItemVoList;              // 주문 품목 목록
    private CartCoupon cartCoupon;                      // 사용 가능 장바구니 쿠폰
    private List<ItemCoupon> itemCouponList;            // 사용 가능 장바구니 쿠폰 목록
}
