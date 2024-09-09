package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.CartItemDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartItemMapperTest {
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartItemMapperTest(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    @Test
    @DisplayName("빈 주입 테스트")
    public void beanInjectionTest() {
        assertNotNull(cartItemMapper);
    }
    /*
        장바구니 상품 CRUD 테스트
        1. 저장
        2. 조회
        3. 카운트
        4. 수량 변경
        5. 삭제
     */
    /*
        저장 테스트
            실패 케이스
            오류 케이스
            성공 케이스

        # 실패 케이스
        삽입 전과 DB 조회 데이터 불일치

        # 오류 케이스
        중복 키 에러
        데이터 무결성 에러
            not null
            도메인 제약

        # 성공 케이스
        삽입 전과 DB 조회 데이터 일치
     */

    CartItemDto cartItem1;
    // 삽입 전 리스트
    List<CartItemDto> insertList = new ArrayList<>();
    int testCnt = 100;

    @BeforeEach
    public void initUser(){
        String userId = "user1";
        String prodNo = "P000";
        String itemNo = "I000";

        // 반환 값이 0
        cartItem1 = new CartItemDto();
        cartItem1.setCartSeq(1);
        cartItem1.setUserId(userId);
        cartItem1.setProdNo(prodNo + "1");
        cartItem1.setItemNo(itemNo + "1");
        cartItem1.setQty(1);

        for (int i = 1; i <= testCnt; i++) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setUserId(userId);
            cartItemDto.setCartSeq(i);
            cartItemDto.setProdNo(prodNo + i);
            cartItemDto.setItemNo(itemNo + i);
            cartItemDto.setQty(i);
            insertList.add(cartItemDto);
        }
    }

    @AfterEach
    public void deleteAllTable(){
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());
    }

    @Test
    @DisplayName("삽입 전과 DB 조회 데이터 일치")
    public void insertSuccessTest(){
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        // 상품 추가
        cartItemMapper.insertCartItem(cartItem1);
        assertEquals(1, cartItemMapper.countAll());

        // 조회
        List<CartItemDto> targetList = cartItemMapper.selectUserCart("user1");
        assertEquals(cartItem1, targetList.get(0));
    }

    @Test
    @DisplayName("중복 키 에러")
    public void insertDuplicateKeyException() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        // 상품 추가
        cartItemMapper.insertCartItem(cartItem1);
        assertEquals(1, cartItemMapper.countAll());

        // 동일한 seq로 상품 추가
        assertThrows(DuplicateKeyException.class, () -> {
            cartItemMapper.insertCartItem(cartItem1);
        });
        assertEquals(1, cartItemMapper.countAll());

        // 조회 후 비교
        List<CartItemDto> targetList = cartItemMapper.selectUserCart(cartItem1.getUserId());
        assertEquals(cartItem1, targetList.get(0));
    }

    @Test
    @DisplayName("데이터 무결성 에러")
    public void insertDataIntegrityException() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        // 반환 값이 0
        cartItem1.setUserId("user111111111111111111111111111111111111111111111111111111");
        cartItem1.setProdNo("P0001111111111111111111111111111111111111111111111111111111");

        // 상품 추가
        assertThrows(DataIntegrityViolationException.class, () -> {
            cartItemMapper.insertCartItem(cartItem1);
        });
        // 실패 했는지 확인
        assertEquals(0, cartItemMapper.countAll());
    }


    @Test
    @DisplayName("장바구니 저장 시 DataIntegrityVioletException 발생")
    public void addCartExceptionFailTest() {
        CartItemDto addItem = new CartItemDto();

        // 필수 값이 없으면 예외가 발생하는지 확인
        // 필수 값 : userId, prodNo, itemNo

        // 모든 값이 없는 경우
        assertThrows(DataIntegrityViolationException.class, () -> {
            cartItemMapper.insertCartItem(addItem);
        });

        // userId만 있는 경우
        addItem.setUserId(cartItem1.getUserId());
        assertThrows(DataIntegrityViolationException.class, () -> {
            cartItemMapper.insertCartItem(addItem);
        });

        // 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        // userId, prodNo만 있는 경우
        addItem.setProdNo(cartItem1.getProdNo());
        assertThrows(DataIntegrityViolationException.class, () -> {
            cartItemMapper.insertCartItem(addItem);
        });

        // 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        // userId, prodNo, itemNo만 있는 경우
        addItem.setItemNo(cartItem1.getItemNo());
        assertThrows(DataIntegrityViolationException.class, () -> {
            cartItemMapper.insertCartItem(addItem);
        });
    }

    /*
        조회 테스트
        성공 케이스
        1. 삽입 전 데이터와 조회 데이터가 일치
        2. 장바구니 번호로 정렬 되는지 확인

        실패 케이스
        1. 없는 유저 아이디로 조회 시 결과가 0
     */
    @Test
    @DisplayName("장바구니 번호로 내림차 순 정렬 되는지 확인")
    public void selectDataSortIsGood() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        assertEquals(testCnt, cartItemMapper.countAll());

        // DB에서 조회
        List<CartItemDto> targetList = cartItemMapper.selectUserCart(cartItem1.getUserId());

        // 사이즈가 같은가
        assertEquals(insertList.size(), targetList.size());

        // 장바구니 번호 기준 내림차순 정렬
        insertList.sort(new Comparator<CartItemDto>() {
            @Override
            public int compare(CartItemDto o1, CartItemDto o2) {
                return o2.getCartSeq() - o1.getCartSeq();
            }
        });

        // 내림차순으로 정렬 됐는가
        for (int i = 0; i < insertList.size(); i++) {
            assertEquals(insertList.get(i), targetList.get(i));
        }
    }

    @Test
    @DisplayName("없는 유저 아이디로 조회 시 결과가 0")
    public void selectNoneUserId() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        // 장바구니 상품 추가
        for (CartItemDto cartItemDto : insertList) {
            cartItemMapper.insertCartItem(cartItemDto);
        }
        assertEquals(testCnt, cartItemMapper.countAll());

        // 없는 아이디로 조회
        List<CartItemDto> targetList = cartItemMapper.selectUserCart("NoneUser");
        assertEquals(0, targetList.size());
    }

    /*
        카운트 테스트

        성공 케이스
        1. 있는 유저 아이디로 조회

        실패 케이스
        1. 없는 유저 아이디로 조회
     */

    @Test
    @DisplayName("있는 유저 아이디로 조회")
    public void countUserCartSuccess() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartItemMapper.countAll());

        // 유저 아이디로 조회
        assertEquals(testCnt, cartItemMapper.countUserCart(cartItem1.getUserId()));
    }

    @Test
    @DisplayName("없는 유저 아이디로 조회")
    public void countUserCartFail() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartItemMapper.countAll());

        // 없는 유저 아이디로 조회
        assertEquals(0, cartItemMapper.countUserCart(""));
        assertEquals(0, cartItemMapper.countUserCart("NoneUser"));
    }

    /*
        수량 변경 테스트

        성공케이스
        1. 해당 유저의 원하는 상품 수량만 변경
        2. 다른 상품에 영향은 없는가

        실패 케이스
        1. 없는 유저 아이디/없는 장바구니 번호로 변경시 수량 변경이 안됨
     */

    @Test
    @DisplayName("해당 유저의 원하는 상품 수량만 변경")
    public void updateCartSuccess() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartItemMapper.countAll());

        List<CartItemDto> sourceList = cartItemMapper.selectUserCart(cartItem1.getUserId());

        // 수량 변경
        CartItemDto sourceDto = sourceList.get(0);
        int updateQty = 10000;
        sourceDto.setQty(updateQty);
        cartItemMapper.updateCartItem(sourceDto);

        // 조회
        List<CartItemDto> targetList = cartItemMapper.selectUserCart(cartItem1.getUserId());
        CartItemDto targetDto = targetList.get(0);

        assertEquals(sourceDto, targetDto);
        assertEquals(updateQty, targetDto.getQty());
    }

    @Test
    @DisplayName("없는 유저 아이디로 변경시 수량 변경이 안됨")
    public void updateUserCartNoneUserIdFail() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartItemMapper.countAll());

        List<CartItemDto> sourceList = cartItemMapper.selectUserCart(cartItem1.getUserId());

        // 없는 유저 아이디로 변경
        CartItemDto sourceDto = sourceList.get(0);
        int updateQty = 10000;
        sourceDto.setQty(updateQty);
        sourceDto.setUserId("NoneUser");
        assertEquals(0, cartItemMapper.updateCartItem(sourceDto));

        // 장바구니 번호 기준 내림차순 정렬
        insertList.sort(new Comparator<CartItemDto>() {
            @Override
            public int compare(CartItemDto o1, CartItemDto o2) {
                return o2.getCartSeq() - o1.getCartSeq();
            }
        });

        // 조회
        List<CartItemDto> targetList = cartItemMapper.selectUserCart(cartItem1.getUserId());
        assertEquals(testCnt, targetList.size());
        assertEquals(insertList, targetList);
    }

    @Test
    @DisplayName("없는 장바구니 번호로 변경시 수량 변경이 안됨")
    public void updateUserCartNoneCartSeqFail() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartItemMapper.countAll());

        List<CartItemDto> sourceList = cartItemMapper.selectUserCart(cartItem1.getUserId());

        // 없는 유저 아이디로 변경
        CartItemDto sourceDto = sourceList.get(0);
        int updateQty = 10000;
        sourceDto.setQty(updateQty);
        sourceDto.setCartSeq(-1);
        assertEquals(0, cartItemMapper.updateCartItem(sourceDto));

        // 장바구니 번호 기준 내림차순 정렬
        insertList.sort(new Comparator<CartItemDto>() {
            @Override
            public int compare(CartItemDto o1, CartItemDto o2) {
                return o2.getCartSeq() - o1.getCartSeq();
            }
        });

        // 조회
        List<CartItemDto> targetList = cartItemMapper.selectUserCart(cartItem1.getUserId());
        assertEquals(testCnt, targetList.size());
        assertEquals(insertList, targetList);
    }

    /*
        삭제 테스트
        성공 케이스
        1. 존재하는 유저의 장바구니 상품만 삭제

        실패 케이스
        1. 없는 유저의 장바구니 상품 삭제
     */

    @Test
    @DisplayName("존재하는 유저의 장바구니 상품만 삭제")
    public void deleteCartProductExistenceUser() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartItemMapper.countAll());

        // 저장한 목록 조회
        List<CartItemDto> sourceList = cartItemMapper.selectUserCart(cartItem1.getUserId());

        // 삭제
        for (CartItemDto cartItemDto : sourceList){
            cartItemMapper.deleteCartItem(cartItemDto);
        }
        // 정상적으로 삭제 됐는지 확인
        assertEquals(0, cartItemMapper.selectUserCart(cartItem1.getUserId()).size());
    }

    @Test
    @DisplayName("없는 유저의 장바구니 상품 삭제")
    public void deleteCartProductNoneExistenceUser() {
        // DB 초기화
        cartItemMapper.deleteAll();
        assertEquals(0, cartItemMapper.countAll());

        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartItemMapper.insertCartItem(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartItemMapper.countAll());

        // 저장한 목록 조회
        List<CartItemDto> sourceList = cartItemMapper.selectUserCart(cartItem1.getUserId());

        // 없는 유저 아이디로 변경
        for (CartItemDto cartItemDto : sourceList){
            cartItemDto.setUserId("NoneUser");
        }

        // 삭제
        for (CartItemDto cartItemDto : sourceList){
            cartItemMapper.deleteCartItem(cartItemDto);
        }
        // 삭제 안 됐는지 확인
        assertEquals(testCnt, cartItemMapper.selectUserCart(cartItem1.getUserId()).size());
    }
}