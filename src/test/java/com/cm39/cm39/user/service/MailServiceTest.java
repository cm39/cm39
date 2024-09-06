package com.cm39.cm39.user.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @DisplayName("코드 생성 테스트")
    @Test
    void createCode() {
        String code = mailService.createCode();
        String code2 = mailService.createCode();

        // 랜덤하게 생성된 코드 서로 불일치해야 함
        assertTrue(!code.equals(code2));
    }

    @Test
    void sendEmail() {
        String email = "seon.hannn@gmail.com";
        try {
            String code = mailService.sendEmail(email);
            assertNotNull(code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void verifyCode() {
        String email = "seon.hannn@gmail.com";
        try {
            String code = mailService.sendEmail(email);
            assertNotNull(code);
            mailService.verifyCode(code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}