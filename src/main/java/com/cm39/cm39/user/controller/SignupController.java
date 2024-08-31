package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    UserDetailService userDetailService;

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public void signup(UserDto userDto) {
        userDetailService.signup(userDto);
    }
}
