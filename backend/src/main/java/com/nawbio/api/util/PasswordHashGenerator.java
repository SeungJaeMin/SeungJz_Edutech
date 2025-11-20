package com.nawbio.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 패스워드 해시 생성 유틸리티
 * 초기 관리자 계정 생성 시 사용
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 기본 관리자 계정 비밀번호 해시 생성
        String[] passwords = {
            "admin123",      // SUPER_ADMIN
            "admin456",      // ADMIN
            "editor789"      // EDITOR
        };

        System.out.println("=== BCrypt Password Hashes ===");
        for (String password : passwords) {
            String hash = encoder.encode(password);
            System.out.println("Password: " + password);
            System.out.println("Hash: " + hash);
            System.out.println();
        }
    }
}
