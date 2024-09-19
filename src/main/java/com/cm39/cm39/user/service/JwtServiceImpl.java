package com.cm39.cm39.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cm39.cm39.exception.user.TokenException;
import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.exception.user.UserExceptionMessage;
import com.cm39.cm39.user.domain.Role;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class JwtServiceImpl implements JwtService {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERID_CLAIM = "userId";
    private static final String BEARER = "Bearer ";
    // secret key
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private long accessTokenValiditySeconds;
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    @Autowired
    private UserMapper userMapper;

    // access token 생성
    @Override
    public String createAccessToken(String userId) {
        return JWT.create() // jwt 토큰 생성
                .withSubject(ACCESS_TOKEN_SUBJECT) // jwt subject 지정
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValiditySeconds)) // 만료시간 설정 (accessTokenValiditySeconds 시간 후에 만료)
                .withClaim(USERID_CLAIM, userId) // claim 추가
                .withClaim("roles", List.of(Role.ADMIN.getValue()))
                .sign(Algorithm.HMAC512(secret)); // HMAC512 알고리즘으로 암호화
    }

    // request token 생성
    @Override
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000))
                .sign(Algorithm.HMAC512(secret));
    }

    // request token 재발급
    @Override
    public void updateRefreshToken(String userId, String refreshToken) {
        UserDto userDto = userMapper.selectUserByUserId(userId);
        if (userDto == null) {
            throw new UserException(UserExceptionMessage.ACCOUNT_NOT_FOUND.getMessage());
        }
        userDto.setRefreshToken(refreshToken);
        updateUserRefreshToken(userId, refreshToken);
    }

    // request token 제거 - 로그아웃
    @Override
    public void destroyRefreshToken(String userId) {
        UserDto userDto = userMapper.selectUserByUserId(userId);
        if (userDto == null) {
            throw new UserException(UserExceptionMessage.ACCOUNT_NOT_FOUND.getMessage());
        }
        userDto.destroyRefreshToken();
        updateUserRefreshToken(userId, null);
    }

    // access token, request token 발급 - 로그인
    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String userId, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, userId, refreshToken);
        updateUserRefreshToken(userId, refreshToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);
    }

    // access token 발급
    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    // get access token
    @Override
    public String extractAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(accessHeader);

        if (accessToken != null && accessToken.startsWith(BEARER)) {
            String token = accessToken.substring(BEARER.length());
            isTokenValid(token);
            userIdValid(token);
            return token;
        }

        return null;
    }

    // get refresh token
    @Override
    public String extractRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(refreshHeader);

        if (refreshToken != null && refreshToken.startsWith(BEARER)) {
            String token = refreshToken.substring(BEARER.length());
            isTokenValid(token);
            return token;
        }

        return null;
    }

    // get user id
    @Override
    public String extractUserId(String accessToken) {
        try {
            isTokenValid(accessToken);
            userIdValid(accessToken);

            return JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(accessToken)
                    .getClaim(USERID_CLAIM)
                    .asString();
        } catch (Exception e) {
            throw new TokenException(UserExceptionMessage.INVALID_VERIFY_TOKEN.getMessage());
        }
    }

    // access token add header
    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, BEARER + accessToken);
    }

    // refresh token add header
    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String userId, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    // update db
    public void updateUserRefreshToken(String userId, String refreshToken) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("refreshToken", refreshToken);
        params.put("upId", userId);
        userMapper.updateRefreshToken(params);
    }

    // token 검증
    @Override
    public void isTokenValid(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
        } catch (Exception e) {
            throw new TokenException(UserExceptionMessage.INVALID_VERIFY_TOKEN.getMessage());
        }
    }

    // user id 클레임 검증
    public void userIdValid(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
        // userId 클레임이 있는지 확인
        String userId = decodedJWT.getClaim(USERID_CLAIM).asString();
        if (userId == null || userId.isEmpty()) {
            throw new TokenException(UserExceptionMessage.INVALID_VERIFY_TOKEN.getMessage());
        }
    }
}
