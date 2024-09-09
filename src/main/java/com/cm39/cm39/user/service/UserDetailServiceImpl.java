package com.cm39.cm39.user.service;

import com.cm39.cm39.exception.user.AlreadyExistsUserException;
import com.cm39.cm39.exception.user.UserNotFoundException;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // userId로 회원 정보 조회
    public UserDto findUserByUserId(String userId) {
        UserDto selectedUser = userMapper.selectUserByUserId(userId);

        if (selectedUser == null) {
            // user not found
            return null;
        }

        return selectedUser;
    }

    // 회원가입
    @Transactional
    public String signup(UserDto userDto) {
        if (userDto == null) {
            throw new UserNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        // 이미 존재하는 계정인지 검증
        UserDto selectedUser = findUserByUserId(userDto.getUserId());

        if (selectedUser != null) {
            throw new AlreadyExistsUserException(selectedUser.getSnsTypeCode() + " 이미 가입한 회원입니다.");
        }

        // 비밀번호 암호화
        userDto.setPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        // insert
        userMapper.insertUser(userDto);

        return userDto.getUserId();
    }
}
