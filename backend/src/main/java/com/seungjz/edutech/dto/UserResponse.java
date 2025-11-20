package com.seungjz.edutech.dto;

import com.seungjz.edutech.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider;
    private String preferredLanguage;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .provider(user.getProvider())
                .preferredLanguage(user.getPreferredLanguage())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
