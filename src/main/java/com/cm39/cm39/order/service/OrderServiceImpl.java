package com.cm39.cm39.order.service;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.order.dto.*;
import com.cm39.cm39.order.exception.OrderException;
import com.cm39.cm39.order.exception.OrderNumGenerateException;
import com.cm39.cm39.order.mapper.OrderItemMapper;
import com.cm39.cm39.order.mapper.OrderMapper;
import com.cm39.cm39.order.vo.OrderFormItemVo;
import com.cm39.cm39.promotion.domain.CartCoupon;
import com.cm39.cm39.promotion.domain.ItemCoupon;
import com.cm39.cm39.promotion.service.CouponService;
import com.cm39.cm39.promotion.service.MileageService;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.cm39.cm39.exception.user.UserExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final String successCallbackUrl = "/payment/success";
    private final String failCallbackUrl = "/payment/fail";

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private final PaymentService paymentService;
    private final MileageService mileageService;
    private final CouponService couponService;
    /*
        1. 주문서 정보 생성
        2. 주문 정보 검증
        3. 주문 정보 생성
        4. 주문 내역 조회
        5. 주문 상세 조회

        6.

        주문/결제 프로세스

            1. 주문서 요청 : 장바구니 / 상품 상세
                #. 요청
                    API : POST /order/orderForm
                    body : [{상품번호, 품목번호, 수량}...]
                    event : 장바구니 / 상품 상세 [주문하기] 클릭
                    description :
                        1. 장바구니, 상품 상세 페이지에서 품목/수량 선택
                        2. 주문하기 버튼 클릭
                        3. 품목 목록 정보로 주문서 페이지 요청

                #. 응답
                    # success
                    body : {주문자:{}, 가용 적립금:{}, 보유 쿠폰:[], 장바구니 품목:[]}
                    description :
                        1. 주문에 필요한 기본 정보
                        2. 주문자, 장바구니 품목, 가용 적립금, 쿠폰, //배송지 목록, 결제 수단 정보//

                    # failed
                    body : 서버 에러

            2. 주문 요청 : 주문서
                #. 요청
                    API : POST /order/orderReady
                    body : {주문자, 결제수단, 결제금액, 장바구니쿠폰, 사용 적립금, 품목 목록, 수령지}
                    event : 주문서 [결제하기] 클릭
                    description :
                        1. 주문서에서 추가 정보 입력 (배송지 정보, 사용 적립금, 사용 쿠폰, 결제 수단, 현금영수증, 개인정보 이용 동의)
                        2. 결제하기 버튼 클릭
                        3. 주문 정보로 주문 요청
                        4. 주문 정보 to 결제 정보 계산 / 유효성 검증(품목 검증(재고, 금액), 결제 정보 검증, 적립금 검증, 쿠폰 검증)

                #. 응답
                    # success
                    body : {주문번호, 주문자, 배송지, 발생 적립금, 배송비, 쿠폰 할인금액, 상품 할인금액, 최종결제금액, 결제수단, 품목 목록}

                    # failed
                    body : 유효성 검증 실패, 서버 에러

            3. 결제 생성 요청 : 주문서
                #. 요청
                    API : POST /order/session
                    body : {주문번호, 주문자 정보, 상품 목록, 결제금액, 부가세}
                    event : /order/orderReady 요청 성공 시
                    description :
                        1. 주문 요청 후 Success 응답 시 요청
                        2. 결제 정보 저장
                        3. 결제 생성 URL 응답

                #. 응답
                    # success
                    body : {주문번호, paymentKey, 결제금액, 결제 URL}

                    # failed
                    body : 결제 정보 저장 실패, 서버 에러

            4. 결제 승인 요청 : PayService
                #. 요청
                    API : /tossApi/
                    body :
                    event :




            1. 주문서 요청
            2. 주문 기본 정보 응답
            3. 사용자가 추가 정보 입력
                3-1. 수령지 정보, 사용 적립금, 사용 쿠폰

            4. 결제 수단 선택
                4-1. 결제 수단, 현금 영수증, 개인정보 이용 동의

            5. 결제하기 클릭
                5-1. 결제 생성 요청
            6. 결제 생성 요청
            7. 결제 승인 요청
     */

    @Override
    @Transactional
    public OrderFormResponse initOrderForm(String userId, OrderFormRequest request){
        // 주문자, 주문 품목 목록, 가용 적립금, 사용 가능 쿠폰

        // 유저 정보 조회
        UserDto user = userMapper.selectUserByUserId(userId);

        if (user == null)
            throw new UserException(ACCOUNT_NOT_FOUND.getMessage());

        // 가용 적립금 조회
        int availableMileage = mileageService.availableUserMileage(userId);

        // 주문 품목 목록
        List<OrderFormItemVo> orderFormItemVoList = request.getOrderFormItemVoList();

        // 사용 가능 쿠폰
        CartCoupon cartCoupon = couponService.availableUserCartCoupon(userId);
        List<ItemCoupon> itemCouponList = couponService.availableUserItemCoupon(userId);

        return OrderFormResponse.builder()
                .user(user)
                .availableMileage(availableMileage)
                .orderFormItemVoList(orderFormItemVoList)
                .cartCoupon(cartCoupon)
                .itemCouponList(itemCouponList)
                .build();
    }

    @Override
    @Transactional
    public OrderReadyResponse requestOrder(String userId, OrderReadyRequest request) {
        // 주문 정보 검증

        // 상품 할인가 비교 및 저장
        // 주문 요청 상품 리스트
        List<OrderItem> orderItemList = request.getOrderItemList();

        // 리스트의 각각 개별 상품 가격 검증
        // 총 가격 검증
        int totalOrderPrice = validateTotalOrderPrice(orderItemList);

        // 검증 내용으로 수정
        request.setTotalOrderPrice(totalOrderPrice);

        // 주문 번호 생성
        String orderId = orderNumGenerator();

        // DB에 저장
        // 주문 테이블, 주문 상품 테이블 : 주문 대기 상태로 저장
        saveOrderInfo(OrderRequestToOrderDto(orderId, request));
        saveOrderProductInfo(OrderRequestToOrderItemDtoList(orderId, request));

        // 결제 테이블 : 결제 대기 상태로 저장
        savePaymentInfo(OrderRequestToPaymentDto(orderId, request));

        return OrderReadyResponse.builder()
                .paymentCode(request.getPayCode())
                .paymentPrice(totalOrderPrice)
                .orderId(orderId)
                .orderName(request.getOrderName())
                .customerEmail(request.getCustomerEmail())
                .customerName(request.getCustomerName())
                .successUrl(successCallbackUrl)
                .failUrl(failCallbackUrl)
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePaymentInfo(PaymentDto paymentDto) {
        int result = paymentService.insertPayment(paymentDto);

        if(result != 1)
            throw new OrderException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrderProductInfo(List<OrderItemDto> orderItemList) {
        int result = 0;
        for (OrderItemDto orderItemDto : orderItemList){
            result += orderItemMapper.insertOrderItem(orderItemDto);
        }

        if (result != orderItemList.size())
            throw new OrderException();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrderInfo(OrderDto orderDto) {
        int result = orderMapper.insertOrder(orderDto);

        if (result != 1)
            throw new OrderException();
    }

    private int validateTotalOrderPrice(List<OrderItem> orderItemList) {
        int totalOrdPrice = 0;

        for (OrderItem orderItem : orderItemList) {
            totalOrdPrice = totalOrdPrice + orderItem.getItemPrice();
        }

        return totalOrdPrice;
    }

    public OrderDto OrderRequestToOrderDto(String orderId, OrderReadyRequest request){
        return OrderDto.builder()
                .ordNo(orderId)
                .ordName(request.getOrderName())
                .totalOrdPrice(request.getTotalOrderPrice())
                .ordStatCode("주문 대기")
                .build();
    }

    public List<OrderItemDto> OrderRequestToOrderItemDtoList(String orderId, OrderReadyRequest request){
        List<OrderItemDto> result = new ArrayList<>();

        for (int i = 0; i < request.getOrderItemList().size(); i++){
            OrderItem orderItem = request.getOrderItemList().get(i);

            OrderItemDto orderItemDto = OrderItemDto.builder()
                    .ordNo(orderId)
                    .ordItemSeq(i)
                    .prodNo(orderItem.getProductNo())
                    .itemNo(orderItem.getItemNo())
                    .itemPrice(orderItem.getItemPrice())
                    .itemQty(orderItem.getQty())
                    .build();

            result.add(orderItemDto);
        }

        return result;
    }

    public PaymentDto OrderRequestToPaymentDto(String orderId, OrderReadyRequest request){
        return PaymentDto.builder()
                .payNo("P" + orderId)
                .ordNo(orderId)
                .payCode(request.getPayCode())
                .payPrice(request.getTotalOrderPrice())
                .payStatCode("결제 대기")
                .build();
    }

    // 주문번호 생성
    public synchronized String orderNumGenerator(){
        try {
            // 쓰레드를 1mills 재운다.
            Thread.sleep(1);

            // 현재 시간 구하기
            LocalDateTime srcTime = LocalDateTime.now();

            // 포맷 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS");
            // 포맷해서 반환
            return srcTime.format(formatter);
        } catch (Exception e) {
            throw new OrderNumGenerateException(e);
        }
    }
}
