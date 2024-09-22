package com.cm39.cm39.order.controller;

import com.cm39.cm39.order.domain.ApiResponse;
import com.cm39.cm39.order.dto.*;
import com.cm39.cm39.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/*
        1. 주문서 정보 생성
        2. 주문 정보 검증
        3. 주문 정보 생성
        4. 주문 내역 조회
        5. 주문 상세 조회

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
                        2. 주문자, 장바구니 품목, 가용 적립금, 장바구니/품목 쿠폰, 배송지 정보// 결제 수단 정보//

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
                        4. 주문 정보 유효성 검증(품목 검증(재고, 금액), 결제 정보 검증, 적립금 검증, 쿠폰 검증)

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
@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // todo: 테스트용 view
    @GetMapping("/order/orderForm")
    public String home(){
        return "/order/testOrderForm";
    }

    @ResponseBody
    @PostMapping("/order/orderForm")
    public ResponseEntity<ApiResponse<?>> orderForm(String userId, @Valid @RequestBody OrderFormRequest request){
        // 주문서 초기화
        OrderFormResponse response = orderService.initOrderForm(userId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .result("SUCCESS")
                        .data(response)
                        .build());
    }

    @ResponseBody
    @PostMapping("/order/orderReady")
    public ResponseEntity<ApiResponse<?>> orderReady(@RequestBody OrderReadyRequest orderReadyRequest) {

        // todo: 추후 로그인한 user 정보에서 가져 올 것
        String userId = "user1";

        // 주문 정보 검증
        OrderReadyResponse response = orderService.requestOrder(userId, orderReadyRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .result("SUCCESS")
                        .data(response)
                        .build());
    }

    // 잘못된 입력 값
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse
                        .builder()
                        .result("FAILED")
                        .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .build());
    }
}