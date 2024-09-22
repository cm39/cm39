package com.cm39.cm39.order.service;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.order.dto.OrderItem;
import com.cm39.cm39.order.dto.OrderReadyRequest;
import com.cm39.cm39.order.mapper.OrderItemMapper;
import com.cm39.cm39.order.mapper.OrderMapper;
import com.cm39.cm39.order.vo.OrderFormItemVo;
import com.cm39.cm39.order.dto.OrderFormRequest;
import com.cm39.cm39.order.dto.OrderFormResponse;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.cm39.cm39.exception.user.UserExceptionMessage.ACCOUNT_NOT_FOUND;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    UserMapper userMapper;
    @Mock
    OrderMapper orderMapper;
    @Mock
    OrderItemMapper orderItemMapper;
    @Mock
    PaymentService paymentService;
    @InjectMocks
    OrderServiceImpl orderService;
    /*
        테스트 시나리오
        테스트 대상 : OrderService 클래스의 initOrder 메소드

        대상 메소드의 역할 : 유저 아이디와 구매하려는 품목 목록으로 주문서 정보를 만들어 준다.
        세부 기능 :
            1. userId로 주문자 정보 저장
            2. 구매 희망 품목 목록 저장
            3. 사용 가능 적립금 조회/저장
            4. # 사용 가능 쿠폰 조회/저장
            5. 주문자 정보가 없으면 예외 발생

        성공 테스트
        1. 주문자 정보가 null이 아니면 성공

        예외 테스트
        1. 주문자 정보가 null인 경우 예외 발생
     */
    @Test
    @DisplayName("주문자 정보가 null이 아니면 성공")
    public void orderFormSuccessTest() {
        // given
        // 유저아이디
        String userId = "user1";
        // 사용 가능 적립금
        // 사용 가능 장바구니 쿠폰
        // 주문자 정보
        UserDto userDto = UserDto.builder()
                                .userId(userId)
                                .build();

        given(userMapper.selectUserByUserId(userId)).willReturn(userDto);


        // 주문 희망 품목 리스트
        List<OrderFormItemVo> orderFormItemVoList = new ArrayList<>();
        OrderFormRequest request = new OrderFormRequest(orderFormItemVoList);

        OrderFormResponse testResponse = OrderFormResponse.builder()
                .user(userDto)
                .orderFormItemVoList(orderFormItemVoList)
                .build();

        // when
        // 주문서 초기화
        OrderFormResponse response = orderService.initOrderForm(userId, request);

        // then
        // response가 예상한 값인지 확인
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(testResponse);

//        assertThat(response.getUser(), is(userDto));
//        assertThat(response.getAvailableMileage(), is(availableMileage));
//        assertThat(response.getOrderFormItemVoList(), is(orderFormItemVoList));
//        assertThat(response.getCartCoupon(), is(cartCoupon));
//        assertThat(response.getItemCouponList(), is(itemCouponList));

        // verify
        // 메소드 호출 횟수 확인
        verify(userMapper, times(1)).selectUserByUserId(userId);
    }

    @Test
    @DisplayName("주문자 정보가 null인 경우 예외 발생")
    public void orderFormExceptionTest() {
        // given
        // 유저아이디
        String userId = "user1";
        given(userMapper.selectUserByUserId(userId)).willReturn(null);

        // 주문 희망 품목 리스트
        List<OrderFormItemVo> orderFormItemVoList = new ArrayList<>();
        OrderFormRequest request = new OrderFormRequest(orderFormItemVoList);

        // when
        // 주문서 초기화
        UserException exception = assertThrows(UserException.class, ()->{
            orderService.initOrderForm(userId, request);
        });

        // then
        // 예외 메시지 확인
//        assertThat(exception.getMessage(), is(ACCOUNT_NOT_FOUND.getMessage()));
        assertThat(exception.getMessage()).isEqualTo(ACCOUNT_NOT_FOUND.getMessage());

        // verify
        // 메소드 호출 횟수 확인
        verify(userMapper, times(1)).selectUserByUserId(userId);
    }

    @Test
    @DisplayName("정상 요청 응답 객체 생성 테스트")
    public void requestOrderSuccessTest() {
        /*
            정상적인 요청일 시 응답 객체가 정상적으로 생성되는가
         */
        // given
        OrderReadyRequest.builder()
                .payCode("10")
                .userId("user1")
                .customerEmail("user1@user1.com")
                .customerName("홍길동")
                .totalOrderPrice(25000)
                .build();

        // when

        // then

    }

    private List<OrderItem> getTestOrderItemList(){
        List<OrderItem> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            OrderItem.builder()
                    .productNo("P0001")
                    .productName("상의")
                    .itemPrice(5000)
                    .itemNo("I0001")
                    .qty(2)
                    .build();
        }

        return list;
    }

//    @Test
//    @DisplayName("총 주문금액 검증 메소드 테스트")
//    public void validateTotalOrderPriceTest() {
//        List<OrderItem> list = new ArrayList<>();
//
//        // 12500
//        OrderItem orderItem1 = OrderItem.builder()
//                .productNo("P0001")
//                .productName("상의")
//                .itemPrice(12500)
//                .itemNo("I0001")
//                .qty(1)
//                .build();
//
//        // 24000
//        OrderItem orderItem2 = OrderItem.builder()
//                .productNo("P0001")
//                .productName("상의")
//                .itemPrice(6000)
//                .itemNo("I0002")
//                .qty(4)
//                .build();
//        // 253600
//        OrderItem orderItem3 = OrderItem.builder()
//                .productNo("P0001")
//                .productName("상의")
//                .itemPrice(126800)
//                .itemNo("I0003")
//                .qty(2)
//                .build();
//
//        // 290100
//        list.add(orderItem1);
//        list.add(orderItem2);
//        list.add(orderItem3);
//
//        int result = orderService.validateTotalOrderPrice(list);
//        System.out.println(result);
//    }
}