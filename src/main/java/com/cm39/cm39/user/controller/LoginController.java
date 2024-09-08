package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Date;

@RequestMapping("/login")
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/form")
    public String loginForm() {
        return "/user/login";
    }

    @GetMapping("/success")
    public String loginSuccess() {
        return "/user/login_success";
    }
}
