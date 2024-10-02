package com.cm39.cm39.user.service;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.exception.user.UserExceptionMessage;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/*
 * 회원가입
 * 1. 필수정보 입력 검증
 * 2. 필수약관 동의 검증
 * 3. 정보 형식 검증
 * 4. 입력한 email로 DB 조회 - MailService에서 진행
 * 4-1. 이미 존재할 경우 sns type으로 팝업 메시지 노출
 * 4-2. 존재하지 않을 경우 이메일 인증
 * 5. 비밀번호 암호화
 * 6. DB 저장
 * */

@Service
public class UserServiceImpl implements UserDetailsService, SignupService {

    @Autowired
    private UserMapper userMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String userId) {
        UserDto userDto = userMapper.selectUserByUserId(userId);
        if (userDto == null) throw new UserException(UserExceptionMessage.ACCOUNT_NOT_FOUND.getMessage());
        return userDto;
    }

    @Override
    public void checkDuplicationUserId(String userId) {
        UserDto selectedUser = userMapper.selectUserByUserId(userId);
        if (selectedUser != null) {
            throw new UserException(selectedUser.getUserId() + " 이미 가입된 계정입니다.");
        }


    }

    @Override
    public String signup(UserDto userDto) {
        if (userDto == null) throw new UserException(UserExceptionMessage.ACCOUNT_NOT_FOUND.getMessage());

        // 중복 ID 체크
        checkDuplicationUserId(userDto.getUserId());

        // 비밀번호 암호화
        userDto.setPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        // DB 저장
        userMapper.insertUser(userDto);

        return userDto.getUserId();
    }
}
