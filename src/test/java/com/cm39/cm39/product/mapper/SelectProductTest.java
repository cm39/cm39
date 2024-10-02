package com.cm39.cm39.product.mapper;

import com.cm39.cm39.product.domain.ProductFilter;
import com.cm39.cm39.product.domain.ProductSort;
import com.cm39.cm39.product.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SelectProductTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void cateId가_null일때_상품조회() {
        // given
        ProductFilter productFilter = ProductFilter.builder()
                .cateId(null)
                .build();

        // when
        List<ProductDto> productList = productMapper.selectProductList(productFilter);
        for(ProductDto productDto : productList) {
            System.out.println(productDto.getCateId() + "   " + productDto.getProdName());
        }

        // then
        assertNotNull(productList);
    }

    @Test
    void cateId가_0일때_상품조회() {
        // given
        ProductFilter productFilter = ProductFilter.builder()
                .cateId("0")
                .build();

        // when
        List<ProductDto> productList = productMapper.selectProductList(productFilter);
        for(ProductDto productDto : productList) {
            System.out.println(productDto.getCateId() + "   " + productDto.getProdName());
        }

        // then
        assertNotNull(productList);
    }

    @Test
    void cateId가_특정값일때_상품조회() {
        // given
        ProductFilter productFilter = ProductFilter.builder()
                .cateId("4")
                .build();

        // when
        List<ProductDto> productList = productMapper.selectProductList(productFilter);
        for(ProductDto productDto : productList) {
            System.out.println(productDto.getCateId() + "   " + productDto.getProdName());
        }

        // then
        assertNotNull(productList);
    }

    @Test
    void 인기순_정렬일때_상품조회() {
        // given
        ProductFilter productFilter = ProductFilter.builder()
                .cateId("5")
                .sort(ProductSort.POPULAR.getValue())
                .build();

        // when
        List<ProductDto> productList = productMapper.selectProductList(productFilter);
        for(ProductDto productDto : productList) {
            System.out.println(productDto.getCateId() + "   " + productDto.getProdViewQty());
        }

        // then
        assertNotNull(productList);
    }

    @Test
    void 리뷰순_정렬일때_상품조회() {
        // given
        ProductFilter productFilter = ProductFilter.builder()
                .cateId("5")
                .sort(ProductSort.REVIEW.getValue())
                .build();

        // when
        List<ProductDto> productList = productMapper.selectProductList(productFilter);
        for(ProductDto productDto : productList) {
            System.out.println(productDto.getCateId() + "   " + productDto.getRevQty());
        }

        // then
        assertNotNull(productList);
    }

    @Test
    void 최신순_정렬일때_상품조회() {
        // given
        ProductFilter productFilter = ProductFilter.builder()
                .cateId("5")
                .sort(ProductSort.LATEST.getValue())
                .build();

        // when
        List<ProductDto> productList = productMapper.selectProductList(productFilter);
        for(ProductDto productDto : productList) {
            System.out.println(productDto.getCateId() + "   " + productDto.getProdRegDate());
        }

        // then
        assertNotNull(productList);
    }
}