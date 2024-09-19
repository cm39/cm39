package com.cm39.cm39.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
    GOOGLE("google"),
    KAKAO("kakao"),
    EMAIL("email");

    private String value;

    public static ProviderType fromValue(String value) {
        for (ProviderType providerType : ProviderType.values()) {
            if (providerType.value.equalsIgnoreCase(value)) {
                return providerType;
            }
        }
        throw new IllegalArgumentException("Unknown provider: " + value);
    }
}
