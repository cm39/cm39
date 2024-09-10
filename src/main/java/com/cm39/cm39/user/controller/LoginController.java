package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Controller
public class LoginController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/form")
    public String loginForm() {
        return "/user/login";
    }

    @GetMapping("/success")
    public String loginSuccess() {
        return "/user/login_success";
    }
}
