package com.cm39.cm39.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    // login page - 인증 불필요
    @GetMapping("/form")
    public String loginForm() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "form");
        return "/user/sns_test";
    }

    // login success page - 인증 필요
    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> loginSuccess() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
