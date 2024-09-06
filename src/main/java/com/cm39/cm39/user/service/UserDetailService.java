package com.cm39.cm39.user.service;

import com.cm39.cm39.user.domain.UserDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public interface UserDetailService {
    public UserDto findUserByUserId(String userId);

    public String signup(UserDto userDto);
}
