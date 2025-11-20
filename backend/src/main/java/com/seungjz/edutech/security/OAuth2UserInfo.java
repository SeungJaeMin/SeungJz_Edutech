package com.seungjz.edutech.security;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuth2UserInfo {
    private String providerId;
    private String provider;
    private String email;
    private String name;
    private String imageUrl;

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .providerId((String) attributes.get("sub"))
                .provider("GOOGLE")
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("picture"))
                .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .providerId(String.valueOf(attributes.get("id")))
                .provider("KAKAO")
                .email((String) account.get("email"))
                .name((String) profile.get("nickname"))
                .imageUrl((String) profile.get("profile_image_url"))
                .build();
    }
}
