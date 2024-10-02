package com.cm39.cm39.product.mapper;

import com.cm39.cm39.product.domain.ProductFilter;
import com.cm39.cm39.product.dto.ProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductDto> selectProductList(ProductFilter productFilter);
    int countProductList(ProductFilter productFilter);
}
