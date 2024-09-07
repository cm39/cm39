package com.cm39.cm39.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize // HTTP 요청 보안 규칙 설정
                        .requestMatchers("/login/**") // 인증요구 x
                        .permitAll())
//                        .anyRequest() // 나머지 모든 요청에 대해 인증 요구
//                        .authenticated()
                .formLogin(formLogin -> formLogin // 폼 기반 로그인 설정 활성화
                        .loginPage("/login") // 인증되지 않은 사용자가 보호된 리소스에 접근하고자 할 때 리디렉션될 페이지 URL
                        .permitAll()) // 로그인 페이지에 대한 접근을 모든 사용자에게 허용
                .rememberMe(Customizer.withDefaults()); // 사용자가 애플리케이션을 닫고 다시 열어도 로그인 유지하도록

        return http.build(); // spring security의 기본 설정 제공
    }

    // 비밀번호 해시화 및 비교
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // BCrypt - 암호화 알고리즘
        return new BCryptPasswordEncoder();
    }
}
