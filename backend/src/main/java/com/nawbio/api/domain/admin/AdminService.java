package com.nawbio.api.domain.admin;

import com.nawbio.api.domain.admin.dto.LoginRequest;
import com.nawbio.api.domain.admin.dto.LoginResponse;
import com.nawbio.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Admin admin = adminRepository.findByUsernameAndIsActiveTrue(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        admin.updateLastLogin();
        adminRepository.save(admin);

        String token = jwtUtil.generateToken(admin.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(admin.getUsername())
                .email(admin.getEmail())
                .build();
    }

    @Transactional
    public void createAdmin(String username, String password, String email) {
        Admin admin = Admin.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .isActive(true)
                .build();

        adminRepository.save(admin);
    }

    @Transactional
    public void createOrUpdateAdmin(String username, String password, String email) {
        Admin admin = adminRepository.findByUsernameAndIsActiveTrue(username)
                .orElse(null);

        if (admin == null) {
            // 계정이 없으면 새로 생성
            admin = Admin.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .isActive(true)
                    .build();
        } else {
            // 계정이 있으면 비밀번호만 업데이트
            admin.setPassword(passwordEncoder.encode(password));
            admin.setEmail(email);
        }

        adminRepository.save(admin);
    }
}
