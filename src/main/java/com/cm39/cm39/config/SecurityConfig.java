package com.cm39.cm39.config;

//import com.cm39.cm39.filter.JsonUsernamePasswordAuthenticationFilter;

import com.cm39.cm39.filter.JsonUsernamePasswordAuthenticationFilter;
import com.cm39.cm39.filter.JwtAuthenticationProcessingFilter;
import com.cm39.cm39.handler.LoginFailureHandler;
import com.cm39.cm39.handler.LoginSuccessJWTProvideHandler;
import com.cm39.cm39.user.mapper.UserMapper;
import com.cm39.cm39.user.service.JwtService;
import com.cm39.cm39.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoderConfig passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // jwt 로그인
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 권한 설정
                .authorizeHttpRequests((authorize) -> authorize
                        // 인증 불필요
                        .requestMatchers("/signup", "/", "/login/form")
                        .permitAll()
                        .anyRequest()
                        .hasRole("USER")
                )
                // custom filter
                .addFilterAfter(jsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationProcessingFilter(), JsonUsernamePasswordAuthenticationFilter.class)
                // logout
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                // stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // provider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.passwordEncoder());

        return daoAuthenticationProvider;
    }

    // manager
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = daoAuthenticationProvider();
        return new ProviderManager(provider);
    }

    // handler
    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler() {
        return new LoginSuccessJWTProvideHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    // custom filter - json type request
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() {
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonUsernamePasswordLoginFilter;
    }

    // custom filter - token 발급
    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jsonUsernamePasswordLoginFilter = new JwtAuthenticationProcessingFilter(jwtService, userMapper);
        return jsonUsernamePasswordLoginFilter;
    }
}
