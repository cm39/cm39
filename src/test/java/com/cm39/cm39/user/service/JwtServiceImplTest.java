package com.cm39.cm39.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cm39.cm39.exception.user.TokenException;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceImplTest {

    private static final String BEARER = "Bearer ";
    private final String userId = "tester@test.com";
    private final String USERID_CLAIM = "userId";
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    @DisplayName("access token 생성 테스트")
    @Test
    void createAccessToken() {
        String accessToken = jwtService.createAccessToken(userId);
        System.out.println(accessToken);
        assertNotNull(accessToken);
    }

    @DisplayName("refresh token 생성 테스트")
    @Test
    void createRefreshToken() {
        String refreshToken = jwtService.createRefreshToken();
        System.out.println(refreshToken);
        assertNotNull(refreshToken);
    }

    @DisplayName("refresh token 재발급 테스트")
    @Test
    void updateRefreshToken() {
        // create
        String refreshToken = jwtService.createRefreshToken();
        System.out.println(refreshToken);
        assertNotNull(refreshToken);

        // update
        jwtService.updateRefreshToken(userId, refreshToken);
        UserDto userDto = userMapper.selectUserByUserId(userId);
        assertNotNull(userDto);
        assertEquals(refreshToken, userDto.getRefreshToken());
    }

    @DisplayName("refresh token 제거 테스트")
    @Test
    void destroyRefreshToken() {
        // create
        String refreshToken = jwtService.createRefreshToken();
        System.out.println(refreshToken);
        assertNotNull(refreshToken);

        // update
        jwtService.updateRefreshToken(userId, refreshToken);
        UserDto userDto = userMapper.selectUserByUserId(userId);
        assertNotNull(userDto);
        assertEquals(refreshToken, userDto.getRefreshToken());

        // remove
        jwtService.destroyRefreshToken(userId);
        UserDto removedRefreshTokenUserDto = userMapper.selectUserByUserId(userId);
        assertNull(removedRefreshTokenUserDto.getRefreshToken());
    }

    @DisplayName("access token, request token 생성 및 전달 테스트")
    @Test
    void sendAccessAndRefreshToken() {
        // create tokens
        String accessToken = jwtService.createAccessToken(userId);
        assertNotNull(accessToken);

        String refreshToken = jwtService.createRefreshToken();
        assertNotNull(refreshToken);

        // set
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        jwtService.sendAccessAndRefreshToken(response, userId, accessToken, refreshToken);

        // verify response
        Mockito.verify(response).setHeader(accessHeader, "Bearer " + accessToken);
        Mockito.verify(response).setHeader(refreshHeader, refreshToken);

        UserDto userDto = userMapper.selectUserByUserId(userId);
        assertNotNull(userDto);
        assertEquals(refreshToken, userDto.getRefreshToken());
    }

    @DisplayName("access token 전달 테스트")
    @Test
    void sendAccessToken() {
        // create token
        String accessToken = jwtService.createAccessToken(userId);
        assertNotNull(accessToken);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        jwtService.sendAccessToken(response, accessToken);

        Mockito.verify(response).setHeader(accessHeader, "Bearer " + accessToken);
    }

    @DisplayName("access token 추출 테스트")
    @Test
    void extractAccessToken() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String accessToken = jwtService.createAccessToken(userId);
        Mockito.when(request.getHeader(accessHeader)).thenReturn(BEARER + accessToken);

        String extractedAccessToken = jwtService.extractAccessToken(request);
        assertNotNull(extractedAccessToken);
        assertEquals(extractedAccessToken, accessToken);
    }

    @DisplayName("refresh token 추출 테스트")
    @Test
    void extractRefreshToken() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String refreshToken = jwtService.createRefreshToken();
        Mockito.when(request.getHeader(refreshHeader)).thenReturn(BEARER + refreshToken);

        String extractedRefreshToken = jwtService.extractRefreshToken(request);
        assertNotNull(extractedRefreshToken);
        assertEquals(extractedRefreshToken, refreshToken);
    }

    @DisplayName("user id 추출 테스트")
    @Test
    void extractUserId() {
        String validToken = JWT.create()
                .withClaim(USERID_CLAIM, userId)
                .sign(Algorithm.HMAC512(secret));

        String extractedUserId = jwtService.extractUserId(validToken);

        assertEquals(userId, extractedUserId);
    }

    @DisplayName("user id 추출 - 유효하지 않은 토큰")
    @Test
    void extractUserIdInvalidToken() {
        String invalidToken = "invalidToken";

        assertThrows(TokenException.class, () -> jwtService.extractUserId(invalidToken));
    }

    @DisplayName("user id 추출 - 유효하지 않은 서명")
    @Test
    void extractUserIdInvalidSignature() {
        String invalidSignature = JWT.create()
                .withClaim(USERID_CLAIM, userId)
                .sign(Algorithm.HMAC512("invalidSignature"));

        assertThrows(TokenException.class, () -> jwtService.extractUserId(invalidSignature));
    }

    @DisplayName("user id 추출 - user id 클레임이 없는 JWT")
    @Test
    void extractUserIdNoUserIdClaim() {
        String tokenWithoutUserId = JWT.create()
                .withClaim("otherClaim", "somevalue")
                .sign(Algorithm.HMAC512(secret));

        assertThrows(TokenException.class, () -> jwtService.extractUserId(tokenWithoutUserId));
    }

    @DisplayName("access token set 테스트")
    @Test
    void setAccessTokenHeader() {
        String accessToken = jwtService.createAccessToken(userId);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        jwtService.setAccessTokenHeader(response, accessToken);
        Mockito.verify(response).setHeader(accessHeader, BEARER + accessToken);
    }

    @DisplayName("refresh token set 테스트")
    @Test
    void setRefreshTokenHeader() {
        String refreshToken = jwtService.createRefreshToken();
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        jwtService.setRefreshTokenHeader(response, userId, refreshToken);
    }

    @DisplayName("token 검증 테스트")
    @Test
    void isTokenValid() {
        String refreshToken = jwtService.createRefreshToken();
        jwtService.isTokenValid(refreshToken);
    }
}