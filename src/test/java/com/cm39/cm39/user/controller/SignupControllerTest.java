package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("회원가입 테스트")
@SpringBootTest
class SignupControllerTest {

    @Autowired
    private SignupController signupController;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void deleteAllUser() {
        userMapper.deleteAllUser();
        assertEquals(countUser(), 0);
    }

    @Test
    void sendMail() {
        String email = "seon.hannn@gmail.com";

        try {
            signupController.sendMail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @DisplayName("가입 성공 테스트")
    @Test
    void signupTest() {
        UserDto user = getValidUserDto();
        signupController.signup(user);
        assertEquals(countUser(), 1);
    }

    @DisplayName("userId empty 검증 테스트")
    @Test
    void userIdEmptyTest() {
        UserDto user = getValidUserDto().toBuilder().userId("").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("userId blank 검증 테스트")
    @Test
    void userIdBlankTest() {
        UserDto user = getValidUserDto().toBuilder().userId("    ").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("userId null 검증 테스트")
    @Test
    void userIdNullTest() {
        UserDto user = getValidUserDto().toBuilder().userId(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("userId 형식 검증 테스트")
    @Test
    void userIdInvalidTypeTest() {
        UserDto user = getValidUserDto().toBuilder().userId("test").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("userId 최대 길이 검증 테스트")
    @Test
    void userIdInvalidMaxSizeTest() {
        String longUserId = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest@test.com";
        UserDto user = getValidUserDto().toBuilder().userId(longUserId).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("password empty 검증 테스트")
    @Test
    void passwordEmptyTest() {
        UserDto user = getValidUserDto().toBuilder().pwd("").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("password blank 검증 테스트")
    @Test
    void passwordBlankTest() {
        UserDto user = getValidUserDto().toBuilder().pwd("    ").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("password null 검증 테스트")
    @Test
    void passwordNullTest() {
        UserDto user = getValidUserDto().toBuilder().pwd(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("password 형식 검증 테스트")
    @Test
    void passwordInvalidTypeTest() {
        UserDto user = getValidUserDto().toBuilder().pwd("test").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("userName empty 검증 테스트")
    @Test
    void userNameEmptyTest() {
        UserDto user = getValidUserDto().toBuilder().userName("").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("userName blank 검증 테스트")
    @Test
    void userNameBlankTest() {
        UserDto user = getValidUserDto().toBuilder().userName("  ").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("userName null 검증 테스트")
    @Test
    void userNameNullTest() {
        UserDto user = getValidUserDto().toBuilder().userName(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("birth null 검증 테스트")
    @Test
    void birthNullTest() {
        UserDto user = getValidUserDto().toBuilder().birth(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("birth 과거 일시 검증 테스트")
    @Test
    void birthInvalidDateTest() {
        LocalDateTime futureBirth = LocalDateTime.of(2040, 1, 8, 0, 0, 0);
        UserDto user = getValidUserDto().toBuilder().birth(futureBirth).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("gndr empty 검증 테스트")
    @Test
    void genderEmptyTest() {
        UserDto user = getValidUserDto().toBuilder().gndr("").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("gndr blank 검증 테스트")
    @Test
    void genderBlankTest() {
        UserDto user = getValidUserDto().toBuilder().gndr("   ").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("gndr null 검증 테스트")
    @Test
    void genderNullTest() {
        UserDto user = getValidUserDto().toBuilder().gndr(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("telNo empty 검증 테스트")
    @Test
    void telNoEmptyTest() {
        UserDto user = getValidUserDto().toBuilder().telNo("").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("telNo blank 검증 테스트")
    @Test
    void telNoBlankTest() {
        UserDto user = getValidUserDto().toBuilder().telNo("   ").build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("telNo null 검증 테스트")
    @Test
    void telNoNullTest() {
        UserDto user = getValidUserDto().toBuilder().telNo(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("mntTrmsAgrDate 미래 일시 검증 테스트")
    @Test
    void mntTrmsAgrDateInvalideDateTest() {
        LocalDateTime futureDate = LocalDateTime.of(2040, 1, 8, 0, 0, 0);
        UserDto user = getValidUserDto().toBuilder().mntTrmsAgrDate(futureDate).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("mntTrmsAgrDate null 검증 테스트")
    @Test
    void mntTrmsAgrDateNullTest() {
        UserDto user = getValidUserDto().toBuilder().mntTrmsAgrDate(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("prsnlInfoAgrDate 미래 일시 검증 테스트")
    @Test
    void prsnlInfoAgrDateInvalideDateTest() {
        LocalDateTime futureDate = LocalDateTime.of(2040, 1, 8, 0, 0, 0);
        UserDto user = getValidUserDto().toBuilder().prsnlInfoAgrDate(futureDate).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("prsnlInfoAgrDate null 검증 테스트")
    @Test
    void prsnlInfoAgrDateNullTest() {
        UserDto user = getValidUserDto().toBuilder().prsnlInfoAgrDate(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("adInfoRcvAgrDate 미래 일시 검증 테스트")
    @Test
    void adInfoRcvAgrDateInvalidDateTest() {
        LocalDateTime futureDate = LocalDateTime.of(2040, 1, 8, 0, 0, 0);
        UserDto user = getValidUserDto().toBuilder().adInfoRcvAgrDate(futureDate).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    @DisplayName("adInfoRcvAgrDate null 검증 테스트")
    @Test
    void adInfoRcvAgrDateNullTest() {
        UserDto user = getValidUserDto().toBuilder().adInfoRcvAgrDate(null).build();
        assertThrows(ConstraintViolationException.class, () -> signupController.signup(user));
    }

    int countUser() {
        return userMapper.selectAllUser().size();
    }

    UserDto getValidUserDto() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime birth = LocalDateTime.of(2002, 1, 8, 0, 0, 0);
        String userId = "tester@test.com";

        return UserDto.builder()
                .userId(userId)
                .grdId("grade1")
                .pwd("test1234!")
                .userName("tester")
                .birth(birth)
                .gndr("F")
                .telNo("01012345678")
                .userStatCode("100")
                .providerTypeCode("email")
                .prsnlInfoAgrDate(now)
                .mntTrmsAgrDate(now)
                .adInfoRcvAgrDate(now)
                .regId(userId)
                .upId(userId)
                .build();
    }
}
