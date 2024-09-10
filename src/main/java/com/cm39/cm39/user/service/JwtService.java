package com.cm39.cm39.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {
    // create
    String createAccessToken(String userId);
    String createRefreshToken();

    // update
    void updateRefreshToken(String userId, String refreshToken);

    // destroy
    void destroyRefreshToken(String userId);

    // send
    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);
    void sendAccessToken(HttpServletResponse response, String accessToken);

    // extract
    String extractAccessToken(HttpServletRequest request);
    String extractRefreshToken(HttpServletRequest request);
    String extractUserId(String accessToken);

    // set
    void setAccessTokenHeader(HttpServletResponse response, String accessToken);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    // validation
    boolean isTokenValid(String token);

}
