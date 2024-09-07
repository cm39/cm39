package com.cm39.cm39.user.service;

import com.cm39.cm39.exception.user.AlreadyExistsUserException;
import com.cm39.cm39.exception.user.FailSendEmailException;
import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.exception.user.UserExceptionMessage;
import com.cm39.cm39.user.mapper.UserMapper;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/*
    이메일 인증
    1. 코드 생성
    2. 이메일 전송
        2-1. 중복 이메일 체크
        2-2. 이메일 메시지 생성
        2-3. 이메일 전송
    3. 코드 검증
*/

@Service
@RequiredArgsConstructor
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private final UserMapper userMapper;

    private static final String senderEmail = "seon.hannn@gmail.com";

    private final String code = createCode();

    // 코드 생성
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) { // 인증코드 총 8자리
            int index = random.nextInt(3); // 0~2 랜덤, 랜덤값으로 switch문 실행
            switch (index) {
                case 0 -> key.append((char) ((int) random.nextInt(26) + 97));
                case 1 -> key.append((char) (int) random.nextInt(26) + 65);
                case 2 -> key.append(random.nextInt(9));
            }
        }

        return key.toString();
    }

    // 중복 email 체크
    private void checkDuplicatedEmail(String email) {
        if (userMapper.selectUserByUserId(email) != null) {
            throw new AlreadyExistsUserException(UserExceptionMessage.EXISTING_ACCOUNT.getMessage());
        }
    }

    // email 내용
    private MimeMessage createEmailMessage(String from, String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("회원가입 이메일 인증");

        String msgg = "";
        msgg += "<h2>cm39</h2>\n" +
                "    <div>안녕하세요. cm39팀입니다.</div>\n" +
                "    <div>회원가입 화면으로 돌아가 아래 인증 코드를 입력해 주세요.</div>\n" +
                "    <div>감사합니다.</div>";
        msgg += "<h3>" + code + "</h3>";    // 메일 인증번호
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress(from, "[cm39] 회원가입 이메일 인증 코드입니다."));

        return message;
    }

    // email 전송
    public String sendEmail(String email) {
        checkDuplicatedEmail(email);

        try {
            MimeMessage message = createEmailMessage(senderEmail, email);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new FailSendEmailException(UserExceptionMessage.FAIL_SEND_CODE.getMessage());
        } catch (UnsupportedEncodingException e) {

        } catch (MailException e) {
            throw new FailSendEmailException(UserExceptionMessage.FAIL_SEND_MAIL.getMessage());
        }

        return code;
    }

    // 코드 검증
    public boolean verifyCode(String verifyCode) {
        return code == verifyCode;
    }
}
