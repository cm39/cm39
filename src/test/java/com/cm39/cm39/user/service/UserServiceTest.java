package com.cm39.cm39.user.service;

import com.cm39.cm39.exception.user.AlreadyExistsUserException;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    String userId = "tester@test.com";

    @BeforeEach
    void deleteAllUser() {
        userMapper.deleteAllUser();
        assertEquals(countUser(), 0);
    }

    @Test
    void loadUserByUsername() {
        UserDto userDto = getUserDto();
        userService.signup(userDto);
        assertEquals(countUser(), 1);

        UserDetails userDetails = userService.loadUserByUsername(userId);
        assertNotNull(userDetails);
        assertEquals(userDto.getUserId(), userDetails.getUsername());
        System.out.println(userDetails);
    }

    @DisplayName("중복 아이디 미존재 테스트")
    @Test
    void checkDuplicationUserId() {
        boolean isDuplication = userService.checkDuplicationUserId(userId);
        assertEquals(isDuplication, false);

    }

    @DisplayName("중복 아이디 존재 테스트")
    @Test
    void checkDuplicationUserIdTrueTest() {
        UserDto userDto1 = getUserDto();
        userService.signup(userDto1);
        assertEquals(countUser(), 1);

        UserDto userDto2 = getUserDto();
        assertThrows(AlreadyExistsUserException.class, () -> userService.signup(userDto2));
    }

    @Test
    void signup() {
        UserDto userDto = getUserDto();
        userService.signup(userDto);
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