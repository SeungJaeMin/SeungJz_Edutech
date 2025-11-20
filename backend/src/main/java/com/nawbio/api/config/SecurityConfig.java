package com.nawbio.api.config;

import com.nawbio.api.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/login", "/auth/login").permitAll()
                        .requestMatchers("/api/init/**", "/init/**").permitAll()  // Admin initialization (dev/test only)
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/news/**", "/news/**").permitAll()
                        .requestMatchers("/api/uploads/images/**", "/uploads/images/**").permitAll()  // Uploaded images
                        .requestMatchers("/api/upload/**", "/upload/**").permitAll()  // Image upload endpoint
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api/swagger-ui/**", "/api/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**", "/api/actuator/**").permitAll()  // Health check
                        .requestMatchers(HttpMethod.POST, "/api/analytics/track", "/analytics/track").permitAll()  // Analytics tracking
                        .requestMatchers(HttpMethod.POST, "/api/contact/submit", "/contact/submit").permitAll()  // Contact form submission
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
