package com.cm39.cm39.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    // 비밀번호 해시화 및 비교
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // BCrypt - 암호화 알고리즘
        return new BCryptPasswordEncoder();
    }
}
