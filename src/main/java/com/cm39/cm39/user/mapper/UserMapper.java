package com.cm39.cm39.user.mapper;

import com.cm39.cm39.user.domain.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    /*
     * 로그인
     * 1. 로그인 버튼 클릭시 아이디, 비밀번호 입력 여부 검증
     * 2. DB에 입력한 id를 가진 계정이 존재하는지 확인
     * 3. 해당 계정의 비밀번호와 입력한 비밀번호가 일치하는지 확인
     * */
    UserDto selectUserByUserId(String userId);

    List<UserDto> selectAllUser();

    /*
     * 회원가입
     * 1. 입력 정보에 대한 형식 검증
     * 2. 필수 정보 입력 검증
     * 3. DB에 해당 userId가 존재하는지 검증
     * 3-1. 이미 존재할 경우, 회원가입 유형에 따라 메시지 노출 ex) 카카오로 가입한 계정입니다.
     * 3-2. 존재하지 않을 경우, DB에 저장
     * */
    void insertUser(UserDto userDto);

    void deleteAllUser();
}
