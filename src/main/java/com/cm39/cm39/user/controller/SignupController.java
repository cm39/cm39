package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.service.MailService;
import com.cm39.cm39.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private MailService mailService;

    // 회원가입 폼을 제공하는 메서드
    @GetMapping("/form")
    public ResponseEntity<String> signupForm() {
        String message = "Signup form loaded successfully.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 이메일 인증을 위한 메일 전송
    @PostMapping("/send-email")
    public ResponseEntity<String> sendMail(@RequestParam String email) {
        String verifyCode = mailService.sendEmail(email);
        String message = "이메일 인증 메일이 전송되었습니다.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 인증 코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String inputCode) {
        if (mailService.verifyCode(inputCode)) {
            return new ResponseEntity<>("인증 완료되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("인증 실패하셨습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 회원가입 요청 처리
    @PostMapping("/request")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDto userDto) {
        String userId = userServiceImpl.signup(userDto);
        if (userId != null) {
            // 회원가입 성공
            return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
        } else {
            // 회원가입 실패
            return new ResponseEntity<>("회원가입에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
