package com.cm39.cm39.user.service;

import com.cm39.cm39.exception.user.AlreadyExistsUserException;
import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.exception.user.UserNotFoundException;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserService implements UserDetailsService, SignupService, LoginService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        UserDto userDto = userMapper.selectUserByUserId(userId);

        if (userDto != null) {
            return userDto;
        } else {
            throw new UserNotFoundException("등록된 회원이 없습니다.");
        }
    }

    @Override
    public boolean checkDuplicationUserId(String userId) {
        UserDto selectedUser = userMapper.selectUserByUserId(userId);

        if (selectedUser != null) {
            // 계정 존재
            throw new AlreadyExistsUserException(selectedUser.getUserId() + " 이미 가입된 계정입니다.");
        }

        return false;
    }

    @Override
    public String signup(UserDto userDto) {
        if (userDto == null) {
            throw new UserException("회원 등록에 실패했습니다.");
        }

        // 중복 ID 체크
        checkDuplicationUserId(userDto.getUserId());

        // 비밀번호 암호화
        userDto.setPwd(bCryptPasswordEncoder.encode(userDto.getPwd()));

        // DB 저장
        userMapper.insertUser(userDto);

        return userDto.getUserId();
    }
}
