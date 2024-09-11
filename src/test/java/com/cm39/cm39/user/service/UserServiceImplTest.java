package com.cm39.cm39.user.service;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("회원 service 테스트")
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserMapper userMapper;

    // 모든 테스트 실행 전 회원 전체 삭제
    @BeforeEach
    void deleteAllUser() {
        userMapper.deleteAllUser();
        assertEquals(countUser(), 0);
    }

    @DisplayName("회원 추가")
    @Test
    void insertUser() {
        UserDto userDto = getUserDto();
        userServiceImpl.signup(userDto);
        assertEquals(countUser(), 1);
    }

    @DisplayName("회원 중복 예외 테스트")
    @Test
    void duplicateEmail() {
        String userId = "test@test.com";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime birth = LocalDateTime.of(2002, 1, 8, 0, 0, 0);

        UserDto user1 = UserDto.builder()
                .userId(userId)
                .grdId("grade1")
                .pwd("test1234!")
                .userName("tester")
                .birth(birth)
                .gndr("F")
                .telNo("01012345678")
                .userStatCode("100")
                .snsTypeCode("100")
                .adInfoRcvAgrDate(now)
                .regId(userId)
                .upId(userId)
                .build();

        UserDto user2 = UserDto.builder()
                .userId(userId)
                .grdId("grade1")
                .pwd("test1234!")
                .userName("tester")
                .birth(birth)
                .gndr("F")
                .telNo("01012345678")
                .userStatCode("100")
                .snsTypeCode("100")
                .adInfoRcvAgrDate(now)
                .regId(userId)
                .upId(userId)
                .build();

        userServiceImpl.signup(user1);
        assertThrows(UserException.class, () -> userServiceImpl.signup(user2));
    }

    // 모든 회원 수 반환
    int countUser() {
        List<UserDto> allUserList = userMapper.selectAllUser();
        return allUserList == null ? 0 : allUserList.size();
    }

    // UserDto 객체 반환
    UserDto getUserDto() {
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
                .snsTypeCode("100")
                .adInfoRcvAgrDate(now)
                .regId(userId)
                .upId(userId)
                .build();
    }
}