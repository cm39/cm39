package com.cm39.cm39.filter;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.exception.user.UserExceptionMessage;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.mapper.UserMapper;
import com.cm39.cm39.user.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> NO_CHECK_URLS = List.of("/login/form", "/", "/signup/**", "/login/oauth2/**", "/oauth2/**", "/favicon.ico");
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public JwtAuthenticationProcessingFilter(JwtService jwtService, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    /*
    1. 로그인
        access token, refresh token 발급

    2. access token 검증
        2-1. access token 만료
            - refresh token 검증
                - refresh token 만료 => 재로그인 필요
                - refresh token 유효 => access token 발급
        2-2. access token 유효 => 유효한 회원
    * */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 지정한 url에 대해 필터링하지 않는다.
        for (String noCheckUrl : NO_CHECK_URLS) {
            if (pathMatcher.match(noCheckUrl, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String accessToken = jwtService.extractAccessToken(request);
        String refreshToken = jwtService.extractRefreshToken(request);

        // access token 만료 => refresh token 검증
        if (accessToken == null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
        }

        // access token 유효 => 유효한 회원
        String userId = jwtService.extractUserId(accessToken);
        UserDto userDto = userMapper.selectUserByUserId(userId);

        if (userDto == null) {
            throw new UserException(UserExceptionMessage.ACCOUNT_NOT_FOUND.getMessage());
        }

        saveAuthentication(userDto);
        filterChain.doFilter(request, response);
    }

    // refresh token 유효 & access token 발급
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) throws IOException {
        UserDto userDto = userMapper.selectUserByRefreshToken(refreshToken);

        if (userDto != null) {
            jwtService.sendAccessToken(response, jwtService.createAccessToken(userDto.getUserId()));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/login/sns");
        }
    }

    private void saveAuthentication(UserDto userDto) {
        UserDto user = UserDto.builder()
                .userId(userDto.getUsername())
                .pwd(userDto.getPassword())
                .role(userDto.getRole()) // 권한
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authoritiesMapper.mapAuthorities(user.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
