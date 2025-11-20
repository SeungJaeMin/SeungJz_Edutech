package com.nawbio.api.controller;

import com.nawbio.api.common.dto.ApiResponse;
import com.nawbio.api.domain.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 계정 초기화 컨트롤러
 * ⚠️ 주의: 이 엔드포인트는 개발/테스트용입니다.
 * 운영 환경에서는 반드시 제거하거나 보안 설정을 추가하세요!
 */
@Slf4j
@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class AdminInitController {

    private final AdminService adminService;

    /**
     * 초기 관리자 계정 생성
     * POST /api/init/admin
     *
     * 생성되는 계정:
     * - username: naw181114
     * - password: naw1023!
     * - email: naw181114@nawbio.com
     */
    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<String>> initAdmin() {
        try {
            adminService.createOrUpdateAdmin("naw181114", "naw1023!", "naw181114@nawbio.com");
            log.info("초기 관리자 계정이 생성/업데이트되었습니다.");
            return ResponseEntity.ok(ApiResponse.success(
                "관리자 계정 생성/업데이트 완료 (username: naw181114)"
            ));
        } catch (Exception e) {
            log.error("관리자 계정 생성 실패", e);
            return ResponseEntity.ok(ApiResponse.error(
                "관리자 계정 생성 실패: " + e.getMessage()
            ));
        }
    }
}
