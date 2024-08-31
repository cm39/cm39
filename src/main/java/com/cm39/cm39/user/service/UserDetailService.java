package com.cm39.cm39.user.service;

import com.cm39.cm39.user.domain.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailService {
    public UserDto findUserByUserId(String userId);

    public void signup(UserDto userDto);
}
