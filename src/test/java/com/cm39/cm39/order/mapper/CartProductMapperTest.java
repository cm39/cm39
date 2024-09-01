package com.cm39.cm39.order.mapper;

import com.cm39.cm39.order.dto.CartProductDto;
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
class CartProductMapperTest {
    private final CartProductMapper cartProductMapper;

    @Autowired
    public CartProductMapperTest(CartProductMapper cartProductMapper) {
        this.cartProductMapper = cartProductMapper;
    }

    @Test
    @DisplayName("빈 주입 테스트")
    public void beanInjectionTest() {
        assertNotNull(cartProductMapper);
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
    @Test
    @DisplayName("삽입 전과 DB 조회 데이터 일치")
    public void insertSuccessTest(){
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 반환 값이 0
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setUserId("user1");
        cartProductDto.setCartSeq(1);
        cartProductDto.setProdNo("P0001");
        cartProductDto.setQty(1);

        // 상품 추가
        cartProductMapper.insertCartProduct(cartProductDto);
        assertEquals(1, cartProductMapper.countAll());

        // 조회
        List<CartProductDto> targetList = cartProductMapper.selectUserCart("user1");
        assertEquals(cartProductDto, targetList.get(0));
    }

    @Test
    @DisplayName("중복 키 에러")
    public void insertDuplicateKeyException() {
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 반환 값이 0
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setUserId("user1");
        cartProductDto.setCartSeq(1);
        cartProductDto.setProdNo("P0001");
        cartProductDto.setQty(1);

        // 상품 추가
        cartProductMapper.insertCartProduct(cartProductDto);
        assertEquals(1, cartProductMapper.countAll());

        // 동일한 seq로 상품 추가
        assertThrows(DuplicateKeyException.class, () -> {
            cartProductMapper.insertCartProduct(cartProductDto);
        });
        assertEquals(1, cartProductMapper.countAll());

        // 조회 후 비교
        List<CartProductDto> targetList = cartProductMapper.selectUserCart("user1");
        assertEquals(cartProductDto, targetList.get(0));
    }

    @Test
    @DisplayName("데이터 무결성 에러")
    public void insertDataIntegrityException() {
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 반환 값이 0
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setUserId("user111111111111111111111111111111111111111111111111111111");
        cartProductDto.setCartSeq(1);
        cartProductDto.setProdNo("P0001111111111111111111111111111111111111111111111111111111");
        cartProductDto.setQty(1);

        // 상품 추가
        assertThrows(DataIntegrityViolationException.class, () -> {
            cartProductMapper.insertCartProduct(cartProductDto);
        });
        // 실패 했는지 확인
        assertEquals(0, cartProductMapper.countAll());
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
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> sourceList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            sourceList.add(cartProductDto);
        }
        assertEquals(testCnt, sourceList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< sourceList.size(); i++){
            cartProductMapper.insertCartProduct(sourceList.get(i));
        }
        assertEquals(testCnt, cartProductMapper.countAll());

        // DB에서 조회
        List<CartProductDto> targetList = cartProductMapper.selectUserCart(userId);

        // 사이즈가 같은가
        assertEquals(sourceList.size(), targetList.size());

        // 장바구니 번호 기준 내림차순 정렬
        sourceList.sort(new Comparator<CartProductDto>() {
            @Override
            public int compare(CartProductDto o1, CartProductDto o2) {
                return o2.getCartSeq() - o1.getCartSeq();
            }
        });

        // 내림차순으로 정렬 됐는가
        for (int i = 0; i < sourceList.size(); i++) {
            assertEquals(sourceList.get(i), targetList.get(i));
        }
    }

    @Test
    @DisplayName("없는 유저 아이디로 조회 시 결과가 0")
    public void selectNoneUserId() {
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> sourceList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            sourceList.add(cartProductDto);
        }
        assertEquals(testCnt, sourceList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< sourceList.size(); i++){
            cartProductMapper.insertCartProduct(sourceList.get(i));
        }
        assertEquals(testCnt, cartProductMapper.countAll());

        // 없는 아이디로 조회
        List<CartProductDto> targetList = cartProductMapper.selectUserCart("");
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
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> sourceList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            sourceList.add(cartProductDto);
        }
        assertEquals(testCnt, sourceList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< sourceList.size(); i++){
            cartProductMapper.insertCartProduct(sourceList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductMapper.countAll());

        // 유저 아이디로 조회
        assertEquals(testCnt, cartProductMapper.countUserCart(userId));
    }

    @Test
    @DisplayName("없는 유저 아이디로 조회")
    public void countUserCartFail() {
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> sourceList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            sourceList.add(cartProductDto);
        }
        assertEquals(testCnt, sourceList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< sourceList.size(); i++){
            cartProductMapper.insertCartProduct(sourceList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductMapper.countAll());

        // 없는 유저 아이디로 조회
        assertEquals(0, cartProductMapper.countUserCart(""));
        assertEquals(0, cartProductMapper.countUserCart("NoneUser"));
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
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            insertList.add(cartProductDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductMapper.insertCartProduct(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductMapper.countAll());

        List<CartProductDto> sourceList = cartProductMapper.selectUserCart(userId);

        // 수량 변경
        CartProductDto sourceDto = sourceList.get(0);
        int updateQty = 10000;
        sourceDto.setQty(updateQty);
        cartProductMapper.updateCartProductQty(sourceDto);

        // 조회
        List<CartProductDto> targetList = cartProductMapper.selectUserCart(userId);
        CartProductDto targetDto = sourceList.get(0);

        assertEquals(sourceDto, targetDto);
        assertEquals(updateQty, targetDto.getQty());
    }

    @Test
    @DisplayName("없는 유저 아이디로 변경시 수량 변경이 안됨")
    public void updateUserCartNoneUserIdFail() {
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            insertList.add(cartProductDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductMapper.insertCartProduct(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductMapper.countAll());

        List<CartProductDto> sourceList = cartProductMapper.selectUserCart(userId);

        // 없는 유저 아이디로 변경
        CartProductDto sourceDto = sourceList.get(0);
        int updateQty = 10000;
        sourceDto.setQty(updateQty);
        sourceDto.setUserId("");
        assertEquals(0, cartProductMapper.updateCartProductQty(sourceDto));

        // 장바구니 번호 기준 내림차순 정렬
        insertList.sort(new Comparator<CartProductDto>() {
            @Override
            public int compare(CartProductDto o1, CartProductDto o2) {
                return o2.getCartSeq() - o1.getCartSeq();
            }
        });

        // 조회
        List<CartProductDto> targetList = cartProductMapper.selectUserCart(userId);
        assertEquals(testCnt, targetList.size());
        assertEquals(insertList, targetList);
    }

    @Test
    @DisplayName("없는 장바구니 번호로 변경시 수량 변경이 안됨")
    public void updateUserCartNoneCartSeqFail() {
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            insertList.add(cartProductDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductMapper.insertCartProduct(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductMapper.countAll());

        List<CartProductDto> sourceList = cartProductMapper.selectUserCart(userId);

        // 없는 유저 아이디로 변경
        CartProductDto sourceDto = sourceList.get(0);
        int updateQty = 10000;
        sourceDto.setQty(updateQty);
        sourceDto.setCartSeq(-1);
        assertEquals(0, cartProductMapper.updateCartProductQty(sourceDto));

        // 장바구니 번호 기준 내림차순 정렬
        insertList.sort(new Comparator<CartProductDto>() {
            @Override
            public int compare(CartProductDto o1, CartProductDto o2) {
                return o2.getCartSeq() - o1.getCartSeq();
            }
        });

        // 조회
        List<CartProductDto> targetList = cartProductMapper.selectUserCart(userId);
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
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            insertList.add(cartProductDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductMapper.insertCartProduct(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductMapper.countAll());

        // 저장한 목록 조회
        List<CartProductDto> sourceList = cartProductMapper.selectUserCart(userId);

        // 삭제
        for (CartProductDto cartProductDto : sourceList){
            cartProductMapper.deleteCartProduct(cartProductDto);
        }
        // 정상적으로 삭제 됐는지 확인
        assertEquals(0, cartProductMapper.selectUserCart(userId).size());
    }

    @Test
    @DisplayName("없는 유저의 장바구니 상품 삭제")
    public void deleteCartProductNoneExistenceUser() {
        // DB 초기화
        cartProductMapper.deleteAll();
        assertEquals(0, cartProductMapper.countAll());

        // 삽입 전 리스트
        List<CartProductDto> insertList = new ArrayList<>();

        int testCnt = 100;
        String userId = "user1";
        for (int i = 1; i <= testCnt; i++) {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(userId);
            cartProductDto.setCartSeq(i);
            cartProductDto.setProdNo("P000" + i);
            cartProductDto.setQty(i);
            insertList.add(cartProductDto);
        }
        assertEquals(testCnt, insertList.size());

        // 장바구니 상품 추가
        for (int i = 0; i< insertList.size(); i++){
            cartProductMapper.insertCartProduct(insertList.get(i));
        }
        // 정상적으로 저장 됐는지 확인
        assertEquals(testCnt, cartProductMapper.countAll());

        // 저장한 목록 조회
        List<CartProductDto> sourceList = cartProductMapper.selectUserCart(userId);

        // 없는 유저 아이디로 변경
        for (CartProductDto cartProductDto : sourceList){
            cartProductDto.setUserId("NoneUser");
        }

        // 삭제
        for (CartProductDto cartProductDto : sourceList){
            cartProductMapper.deleteCartProduct(cartProductDto);
        }
        // 삭제 안 됐는지 확인
        assertEquals(testCnt, cartProductMapper.selectUserCart(userId).size());
    }
}