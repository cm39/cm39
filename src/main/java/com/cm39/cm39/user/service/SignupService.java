package com.cm39.cm39.user.service;

import com.cm39.cm39.user.domain.UserDto;

public interface SignupService {
    String signup(UserDto userDto);

    void checkDuplicationUserId(String userId);
}
