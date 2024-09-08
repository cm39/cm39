package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.domain.UserDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String getTest(@AuthenticationPrincipal UserDto userDto) {
        System.out.println("login user : " + userDto);
        return "/test";
    }

}
