package com.nawbio.api.domain.settings;

import com.nawbio.api.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    /**
     * 관리자 이메일 리스트 조회 (Admin only)
     */
    @GetMapping("/admin-emails")
    public ResponseEntity<ApiResponse<List<String>>> getAdminEmails() {
        List<String> emails = settingsService.getAdminEmails();
        return ResponseEntity.ok(ApiResponse.success("Admin emails retrieved", emails));
    }

    /**
     * 관리자 이메일 리스트 업데이트 (Admin only)
     */
    @PutMapping("/admin-emails")
    public ResponseEntity<ApiResponse<List<String>>> updateAdminEmails(@RequestBody List<String> emails) {
        try {
            List<String> updated = settingsService.updateAdminEmails(emails);
            return ResponseEntity.ok(ApiResponse.success("Admin emails updated", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update admin emails: " + e.getMessage()));
        }
    }

    /**
     * 관리자 이메일 추가 (Admin only)
     */
    @PostMapping("/admin-emails")
    public ResponseEntity<ApiResponse<List<String>>> addAdminEmail(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            List<String> updated = settingsService.addAdminEmail(email);
            return ResponseEntity.ok(ApiResponse.success("Admin email added", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to add admin email: " + e.getMessage()));
        }
    }

    /**
     * 관리자 이메일 삭제 (Admin only)
     */
    @DeleteMapping("/admin-emails/{email}")
    public ResponseEntity<ApiResponse<List<String>>> removeAdminEmail(@PathVariable String email) {
        try {
            List<String> updated = settingsService.removeAdminEmail(email);
            return ResponseEntity.ok(ApiResponse.success("Admin email removed", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to remove admin email: " + e.getMessage()));
        }
    }
}
