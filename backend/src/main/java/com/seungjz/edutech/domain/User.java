package com.seungjz.edutech.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String provider; // GOOGLE, KAKAO

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "preferred_language")
    private String preferredLanguage = "ko";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder
    public User(String email, String provider, String providerId,
                String nickname, String profileImageUrl) {
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = LocalDateTime.now();
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }
}
