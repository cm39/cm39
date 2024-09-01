package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.CartProductOptionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartProductOptionMapperTest {
    private final CartProductOptionMapper cartProductOptionMapper;

    @Autowired
    public CartProductOptionMapperTest(CartProductOptionMapper cartProductOptionMapper) {
        this.cartProductOptionMapper = cartProductOptionMapper;
    }

    @Test
    @DisplayName("빈 주입 테스트")
    public void beanInjectionTest() {
        assertNotNull(cartProductOptionMapper);
    }
    
    /*
        장바구니 상품 옵션 CRUD 테스트
        1. 저장
        2. 조회
        3. 옵션 변경
        4. 삭제
        
        
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
    @Test
    @DisplayName("삽입 전과 DB 조회 데이터 일치")
    public void insertSuccessTest(){
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 반환 값이 0
        CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
        cartProductOptionDto.setUserId("user1");
        cartProductOptionDto.setCartSeq(1);
        cartProductOptionDto.setOptTypeNo("1");
        cartProductOptionDto.setOptDetailSeq(1);

        // 상품 추가
        cartProductOptionMapper.insertCartProductOption(cartProductOptionDto);
        assertEquals(1, cartProductOptionMapper.countAll());

        // 조회
        List<CartProductOptionDto> targetList = cartProductOptionMapper.selectAll();
        assertEquals(cartProductOptionDto, targetList.get(0));
    }

    @Test
    @DisplayName("중복 키 에러")
    public void insertDuplicateKeyException() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 반환 값이 0
        CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
        cartProductOptionDto.setUserId("user1");
        cartProductOptionDto.setCartSeq(1);
        cartProductOptionDto.setOptTypeNo("1");
        cartProductOptionDto.setOptDetailSeq(1);

        // 옵션 추가
        cartProductOptionMapper.insertCartProductOption(cartProductOptionDto);
        assertEquals(1, cartProductOptionMapper.countAll());

        // 동일한 seq로 옵션 추가
        assertThrows(DuplicateKeyException.class, () -> {
            cartProductOptionMapper.insertCartProductOption(cartProductOptionDto);
        });
        assertEquals(1, cartProductOptionMapper.countAll());

        // 조회 후 비교
        List<CartProductOptionDto> targetList = cartProductOptionMapper.selectAll();
        assertEquals(cartProductOptionDto, targetList.get(0));
    }

    @Test
    @DisplayName("데이터 무결성 에러")
    public void insertDataIntegrityException() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 반환 값이 0
        CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
        cartProductOptionDto.setUserId("user111111111111111111111111111111111111111111111111111111");
        cartProductOptionDto.setCartSeq(1);
        cartProductOptionDto.setOptTypeNo("1111111111111111111111111111111111111111111111111111111");
        cartProductOptionDto.setOptDetailSeq(1);

        // 상품 추가
        assertThrows(DataIntegrityViolationException.class, () -> {
            cartProductOptionMapper.insertCartProductOption(cartProductOptionDto);
        });
        // 실패 했는지 확인
        assertEquals(0, cartProductOptionMapper.countAll());
    }
    /*
        수량 변경 테스트

        성공케이스
        1. 해당 유저의 원하는 상품 옵션만 변경
        2. 다른 상품 옵션에 영향은 없는지 확인

        실패 케이스
        1. 없는 유저 아이디/없는 장바구니 번호로 변경시 수량 변경이 안됨
     */

    @Test
    @DisplayName("해당 유저의 원하는 상품 옵션만 변경")
    public void updateCartOptionSuccess() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 삽입 전 리스트
        List<CartProductOptionDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
            cartProductOptionDto.setUserId(userId);
            cartProductOptionDto.setCartSeq(i);
            cartProductOptionDto.setOptTypeNo("" + i);
            cartProductOptionDto.setOptDetailSeq(i);
            insertList.add(cartProductOptionDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductOptionMapper.insertCartProductOption(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductOptionMapper.countAll());

        List<CartProductOptionDto> sourceList = cartProductOptionMapper.selectAll();

        // 수량 변경
        CartProductOptionDto sourceDto = sourceList.get(0);
        int updateQty = 10000;
        sourceDto.setOptDetailSeq(updateQty);
        cartProductOptionMapper.updateCartProductOption(sourceDto);

        // 조회
        List<CartProductOptionDto> targetList = cartProductOptionMapper.selectAll();
        CartProductOptionDto targetDto = sourceList.get(0);

        assertEquals(sourceDto, targetDto);
        assertEquals(updateQty, targetDto.getOptDetailSeq());
    }

    @Test
    @DisplayName("다른 상품 옵션에 영향은 없는지 확인")
    public void updateUserCartOption() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 삽입 전 리스트
        List<CartProductOptionDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
            cartProductOptionDto.setUserId(userId);
            cartProductOptionDto.setCartSeq(i);
            cartProductOptionDto.setOptTypeNo("" + i);
            cartProductOptionDto.setOptDetailSeq(i);
            insertList.add(cartProductOptionDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductOptionMapper.insertCartProductOption(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductOptionMapper.countAll());

        List<CartProductOptionDto> sourceList = cartProductOptionMapper.selectAll();

        // 수량 변경
        CartProductOptionDto sourceDto = sourceList.get(0);
        int updateOptDetailSeq = 10000;
        sourceDto.setOptDetailSeq(updateOptDetailSeq);
        cartProductOptionMapper.updateCartProductOption(sourceDto);

        // 조회
        List<CartProductOptionDto> targetList = cartProductOptionMapper.selectAll();
        CartProductOptionDto targetDto = sourceList.get(0);

        assertEquals(sourceDto, targetDto);
        assertEquals(updateOptDetailSeq, targetDto.getOptDetailSeq());

        // 변경이 다른 옵션에 영향을 안 줬는지 확인
        for (int i = 0; i < sourceList.size(); i++) {
            if (i == 0) continue;
            assertEquals(insertList.get(i), targetList.get(i));
        }
    }

    @Test
    @DisplayName("없는 장바구니 번호로 변경시 수량 변경이 안됨")
    public void updateUserCartNoneCartSeqFail() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 삽입 전 리스트
        List<CartProductOptionDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
            cartProductOptionDto.setUserId(userId);
            cartProductOptionDto.setCartSeq(i);
            cartProductOptionDto.setOptTypeNo("" + i);
            cartProductOptionDto.setOptDetailSeq(i);
            insertList.add(cartProductOptionDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductOptionMapper.insertCartProductOption(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductOptionMapper.countAll());

        List<CartProductOptionDto> sourceList = cartProductOptionMapper.selectAll();

        // 없는 유저 아이디로 변경
        // 수량 변경
        CartProductOptionDto sourceDto = sourceList.get(0);
        String updateUserId = "NoneUser";
        int updateOptDetailSeq = 10000;
        sourceDto.setUserId(updateUserId);
        sourceDto.setOptDetailSeq(updateOptDetailSeq);
        cartProductOptionMapper.updateCartProductOption(sourceDto);

        // 조회
        List<CartProductOptionDto> targetList = cartProductOptionMapper.selectAll();
        assertEquals(testCnt, targetList.size());
        assertEquals(insertList, targetList);
    }

    /*
        조회 테스트
        성공케이스
        1. 유저의 장바구니 상품의 개별 옵션 값은 하나만 존재

     */
    @Test
    @DisplayName("유저의 장바구니 상품의 개별 옵션 값은 하나만 존재")
    public void selectSameOption() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 삽입 전 리스트
        List<CartProductOptionDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
            cartProductOptionDto.setUserId(userId);
            cartProductOptionDto.setCartSeq(i);
            cartProductOptionDto.setOptTypeNo("" + i);
            cartProductOptionDto.setOptDetailSeq(i);
            insertList.add(cartProductOptionDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductOptionMapper.insertCartProductOption(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductOptionMapper.countAll());

        List<CartProductOptionDto> sourceList = cartProductOptionMapper.selectAll();

        // 같은지 확인
        for (CartProductOptionDto sourceDto : sourceList) {
            CartProductOptionDto targetDto = cartProductOptionMapper.selectCartProductSameOption(sourceDto);
            assertEquals(sourceDto, targetDto);
        }
    }

    
    /*
        삭제 테스트
        성공 케이스
        1. 존재하는 유저의 장바구니 옶션만 삭제

        실패 케이스
        1. 없는 유저의 장바구니 옵션 삭제
     */

    @Test
    @DisplayName("존재하는 유저의 장바구니 상품만 삭제")
    public void deleteCartProductExistenceUser() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 삽입 전 리스트
        List<CartProductOptionDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
            cartProductOptionDto.setUserId(userId);
            cartProductOptionDto.setCartSeq(i);
            cartProductOptionDto.setOptTypeNo("" + i);
            cartProductOptionDto.setOptDetailSeq(i);
            insertList.add(cartProductOptionDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductOptionMapper.insertCartProductOption(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductOptionMapper.countAll());

        // 저장한 목록 조회
        List<CartProductOptionDto> sourceList = cartProductOptionMapper.selectAll();

        // 삭제
        for (CartProductOptionDto cartProductOptionDto : sourceList){
            cartProductOptionMapper.deleteCartProductOption(cartProductOptionDto);
        }
        // 정상적으로 삭제 됐는지 확인
        assertEquals(0, cartProductOptionMapper.selectAll().size());
    }

    @Test
    @DisplayName("없는 유저의 장바구니 상품 삭제")
    public void deleteCartProductNoneExistenceUser() {
        // DB 초기화
        cartProductOptionMapper.deleteAll();
        assertEquals(0, cartProductOptionMapper.countAll());

        // 삽입 전 리스트
        List<CartProductOptionDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductOptionDto cartProductOptionDto = new CartProductOptionDto();
            cartProductOptionDto.setUserId(userId);
            cartProductOptionDto.setCartSeq(i);
            cartProductOptionDto.setOptTypeNo("" + i);
            cartProductOptionDto.setOptDetailSeq(i);
            insertList.add(cartProductOptionDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductOptionMapper.insertCartProductOption(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductOptionMapper.countAll());

        // 저장한 목록 조회
        List<CartProductOptionDto> sourceList = cartProductOptionMapper.selectAll();

        // 없는 유저 아이디로 변경
        for (CartProductOptionDto cartProductOptionDto : sourceList){
            cartProductOptionDto.setUserId("NoneUser");
        }

        // 삭제
        for (CartProductOptionDto cartProductOptionDto : sourceList){
            cartProductOptionMapper.deleteCartProductOption(cartProductOptionDto);
        }
        // 삭제 안 됐는지 확인
        assertEquals(testCnt, cartProductOptionMapper.selectAll().size());
    }
}