package com.cm39.cm39.handler;

import com.cm39.cm39.user.domain.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        log.info("로그인 성공, JWT 발급. userId : ", userDto.getUserId());
        response.getWriter().write("success");
    }
}
