package com.cm39.cm39.handler;

import com.cm39.cm39.user.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String URI = "/auth/success";
    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // access token, refresh token 발급
        if (authentication.getPrincipal() instanceof UserDetails) {
            String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
            String accessToken = jwtService.createAccessToken(userId);
            String refreshToken = jwtService.extractRefreshToken(request);
            jwtService.sendAccessAndRefreshToken(response, userId, accessToken, refreshToken);
        }
    }
}
