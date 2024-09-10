package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.service.MailService;
import com.cm39.cm39.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping("/signup")
@Controller
public class SignupController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private MailService mailService;

    @GetMapping("/form")
    public String signupForm() {
        return "/user/signup";
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendMail(String email) {
        String verifyCode = mailService.sendEmail(email);
        String message = "이메일 인증 메일이 전송되었습니다.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/verify-code")
    public String verifyCode(String inputCode) {
        return mailService.verifyCode(inputCode) ? "인증 완료되었습니다." : "인증 실패하셨습니다.";
    }

    @PostMapping("/request")
    public String signup(@Valid UserDto userDto) {
        String userId = userServiceImpl.signup(userDto);
        if (userId != null) {
            // 회원가입 성공 화면으로 redirect
            return "redirect:/signup/complete";
        }
        return "signup";
    }
}
