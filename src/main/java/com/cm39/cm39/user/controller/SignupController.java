package com.cm39.cm39.user.controller;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.exception.user.UserExceptionMessage;
import com.cm39.cm39.exception.user.UserVerifyException;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.service.MailService;
import com.cm39.cm39.user.service.UserDetailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private MailService mailService;

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public void signup(UserDto userDto) {
        userDetailService.signup(userDto);
    }

    @PostMapping("/signup/send-email")
    public ResponseEntity<String> mailConfirm(String email) throws Exception {
        String verifyCode = mailService.sendEmail(email);
        System.out.println("인증코드 : " + verifyCode);
        String message = "이메일 인증 메일이 전송되었습니다.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/signup/verify-code")
    public String verifyCode(String inputCode) {
        return mailService.verifyCode(inputCode) ? "인증 완료되었습니다." : "인증 실패하셨습니다.";
    }
}
