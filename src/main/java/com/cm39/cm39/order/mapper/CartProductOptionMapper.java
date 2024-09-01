package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.CartProductDto;
import com.cm39.cm39.order.dto.CartProductOptionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartProductOptionMapper {
    // 장바구니 상품 옵션 저장
    int insertCartProductOption(CartProductOptionDto cartProductOptionDto);

    // 장바구니 상품 옵션 변경
    int updateCartProductOption(CartProductOptionDto cartProductOptionDto);

    // 같은 옵션 상품이 있는지 조회
    CartProductOptionDto selectCartProductSameOption(CartProductOptionDto cartProductOptionDto);

    // 장바구니 상품 옵션 삭제
    int deleteCartProductOption(CartProductOptionDto cartProductOptionDto);

    // 테스트용
    int deleteAll();
    int countAll();
    List<CartProductOptionDto> selectAll();
}
