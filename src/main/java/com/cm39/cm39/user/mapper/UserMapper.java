package com.cm39.cm39.user.mapper;

import com.cm39.cm39.user.domain.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    // insert
    void insertUser(UserDto userDto);

    // select
    UserDto selectUserByUserId(String userId);

    List<UserDto> selectAllUser();

    UserDto selectUserByRefreshToken(String refreshToken);

    // update
    void updateRefreshToken(Map params);

    // delete
    void deleteAllUser();
}
