package com.cm39.cm39.user.mapper;

import com.cm39.cm39.user.domain.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 mapper 테스트")
//@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    // 모든 테스트 실행 전 회원 전체 삭제
    @BeforeEach
    void deleteAllUser() {
        userMapper.deleteAllUser();
        assertEquals(countUser(), 0);
    }

    @DisplayName("회원Id로 조회")
    @Test
    void selectUserByUserId() {
        // insert user
        UserDto userDto = getUserDto();
        userMapper.insertUser(userDto);

        List<UserDto> allUserList = userMapper.selectAllUser();
        assertNotNull(allUserList);
        assertTrue(allUserList.size() > 0);

        // select user by userId
        UserDto selectedUser = userMapper.selectUserByUserId(userDto.getUserId());
        assertNotNull(selectedUser);
        assertEquals(userDto.getUserId(), selectedUser.getUserId());
        assertEquals(userDto.getPwd(), selectedUser.getPwd());
        assertEquals(userDto.getGrdId(), selectedUser.getGrdId());
        assertEquals(userDto.getUsername(), selectedUser.getUsername());
        assertEquals(userDto.getBirth(), selectedUser.getBirth());
        assertEquals(userDto.getGndr(), selectedUser.getGndr());
        assertEquals(userDto.getTelNo(), selectedUser.getTelNo());
        assertEquals(userDto.getUserStatCode(), selectedUser.getUserStatCode());
        assertEquals(userDto.getSnsTypeCode(), selectedUser.getSnsTypeCode());
        assertEquals(userDto.getRegId(), selectedUser.getRegId());
        assertEquals(userDto.getUpId(), selectedUser.getUpId());
    }

    @DisplayName("모든 회원 조회")
    @Test
    void selectAllUser() {
        // insert user
        UserDto userDto = getUserDto();
        userMapper.insertUser(userDto);

        // select all user
        List<UserDto> allUserList = userMapper.selectAllUser();
        assertNotNull(allUserList);
        assertTrue(allUserList.size() > 0);
    }

    @DisplayName("회원 추가")
    @Test
    void insertUser() {
        UserDto userDto = getUserDto();
        UserDto existingUser = userMapper.selectUserByUserId(userDto.getUserId());

        if (existingUser != null) {
            // 회원가입 불가
        }

        userMapper.insertUser(userDto);
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