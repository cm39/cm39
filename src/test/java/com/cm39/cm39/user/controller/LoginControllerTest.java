package com.cm39.cm39.user.controller;

import com.cm39.cm39.user.domain.Role;
import com.cm39.cm39.user.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    private static final String BEARER = "Bearer ";
    private final String USERID = "tester@test.com";
    private String accessToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accessToken = jwtService.createAccessToken(USERID);
    }

    @DisplayName("로그인 페이지 접근 테스트")
    @Test
    public void testLoginForm() throws Exception {
        mockMvc.perform(get("/login/form")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("로그인 성공 페이지 접근 테스트")
    @Test
    public void testLoginSuccess() throws Exception {
        mockMvc.perform(get("/login/success")
                        .header("Authorization", BEARER + accessToken)
                        .header("roles", List.of(Role.ADMIN.getValue()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("로그아웃 성공 테스트")
    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(post("/logout")
                        .header("Authorization", BEARER + accessToken)
                        .header("roles", List.of(Role.ADMIN.getValue()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
