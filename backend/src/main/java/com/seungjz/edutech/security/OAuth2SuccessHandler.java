package com.seungjz.edutech.security;

import com.seungjz.edutech.domain.User;
import com.seungjz.edutech.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, attributes);

        log.info("OAuth2 Login Success - Provider: {}, Email: {}", userInfo.getProvider(), userInfo.getEmail());

        // 사용자 저장 또는 업데이트
        User user = saveOrUpdate(userInfo);

        // JWT 토큰 생성
        String token = tokenProvider.generateToken(user.getId(), user.getEmail());

        // 프론트엔드로 리다이렉트 (토큰 포함)
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/callback")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User saveOrUpdate(OAuth2UserInfo userInfo) {
        User user = userRepository.findByProviderAndProviderId(
                        userInfo.getProvider(), userInfo.getProviderId())
                .map(existingUser -> {
                    existingUser.updateProfile(userInfo.getName(), userInfo.getImageUrl());
                    existingUser.updateLastLogin();
                    return existingUser;
                })
                .orElse(User.builder()
                        .email(userInfo.getEmail())
                        .provider(userInfo.getProvider())
                        .providerId(userInfo.getProviderId())
                        .nickname(userInfo.getName())
                        .profileImageUrl(userInfo.getImageUrl())
                        .build());

        return userRepository.save(user);
    }
}
