package com.cm39.cm39.order.dto;

import com.cm39.cm39.order.vo.OrderFormItemVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFormRequest {
    private List<OrderFormItemVo> orderFormItemVoList;
}
