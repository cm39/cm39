package com.cm39.cm39.user.service;

import com.cm39.cm39.user.domain.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailService {
    public void insertUser(UserDto userDto);
}
