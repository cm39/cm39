package com.cm39.cm39.user.service;

import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 service 테스트")
@SpringBootTest
class UserDetailServiceImplTest {

    @Autowired
    private UserDetailService userDetailService;

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
        userDetailService.signup(userDto);
        assertEquals(countUser(), 1);
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