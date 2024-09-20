package com.cm39.cm39.user.service;

import com.cm39.cm39.exception.user.UserException;
import com.cm39.cm39.user.domain.ProviderType;
import com.cm39.cm39.user.domain.Role;
import com.cm39.cm39.user.domain.UserDto;
import com.cm39.cm39.user.domain.UserPrincipal;
import com.cm39.cm39.user.info.OAuth2UserInfo;
import com.cm39.cm39.user.info.OAuth2UserInfoFactory;
import com.cm39.cm39.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.fromValue(userRequest.getClientRegistration().getRegistrationId());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        UserDto savedUser = userMapper.selectUserByUserId(userInfo.getEmail());

        if (savedUser != null) {
            // 이미 가입한 경우
            if (!providerType.getValue().equals(savedUser.getProviderTypeCode())) {
                // 다른 방법으로 가입한 경우
                throw new UserException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderTypeCode() + " account to login."
                );
            }
        } else {
            // 가입 정보가 없는 경우
            savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private UserDto createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        UserDto user = UserDto.builder()
                .userId(userInfo.getEmail())
                .grdId("Bronze")
                .userName(userInfo.getName())
                .providerTypeCode(providerType.getValue())
                .role(Role.USER.getValue())
                .build();

        userMapper.insertUser(user);
        return user;
    }
}
