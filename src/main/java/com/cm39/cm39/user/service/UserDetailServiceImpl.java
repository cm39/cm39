package com.cm39.cm39.user.service;

import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * 회원가입
 * 1. 필수정보 입력 검증
 * 2. 필수약관 동의 검증
 * 3. 정보 형식 검증
 * 4. 입력한 email로 DB 조회
 * 4-1. 이미 존재할 경우 sns type으로 팝업 메시지 노출
 * 4-2. 존재하지 않을 경우 가입 성공 + 리다이렉트
 * 5. 비밀번호 암호화
 * 6. DB 저장
 * */

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    public UserDto selectUserByUserId(String userId) {
//        UserDto selectedUser = userMapper.selectUserByUserId(userId);
//        if (selectedUser == null) {
//            // user not found
//        }
//        return selectedUser;
//    }

    @Transactional
    public void insertUser(UserDto userDto) {
        // 비밀번호 암호화
        userDto.setPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));
        if (userDto == null) {
            // user must not null
        }
        userMapper.insertUser(userDto);
    }
}
