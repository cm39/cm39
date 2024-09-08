package com.cm39.cm39.user.service;

import com.cm39.cm39.user.domain.UserDto;

public interface SignupService {
    public String signup(UserDto userDto);
    public boolean checkDuplicationUserId(String userId);
}
