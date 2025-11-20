package com.nawbio.api.domain.contact;

import com.nawbio.api.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    /**
     * 신규 문의 제출 (Public - 인증 불필요)
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<Map<String, Object>>> submitInquiry(@RequestBody ContactRequest request) {
        try {
            ContactInquiry inquiry = contactService.createInquiry(request);

            Map<String, Object> data = new HashMap<>();
            data.put("id", inquiry.getId());
            data.put("message", "문의가 성공적으로 접수되었습니다. 빠른 시일 내에 답변드리겠습니다.");

            return ResponseEntity.ok(ApiResponse.success("Inquiry submitted successfully", data));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to submit inquiry: " + e.getMessage()));
        }
    }

    /**
     * 모든 문의 조회 (Admin only)
     */
    @GetMapping("/inquiries")
    public ResponseEntity<ApiResponse<List<ContactInquiry>>> getAllInquiries() {
        List<ContactInquiry> inquiries = contactService.getAllInquiries();
        return ResponseEntity.ok(ApiResponse.success("Inquiries retrieved successfully", inquiries));
    }

    /**
     * 상태별 문의 조회 (Admin only)
     */
    @GetMapping("/inquiries/status/{status}")
    public ResponseEntity<ApiResponse<List<ContactInquiry>>> getInquiriesByStatus(
            @PathVariable ContactInquiry.InquiryStatus status) {
        List<ContactInquiry> inquiries = contactService.getInquiriesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Inquiries retrieved successfully", inquiries));
    }

    /**
     * 문의 상세 조회 (Admin only)
     */
    @GetMapping("/inquiries/{id}")
    public ResponseEntity<ApiResponse<ContactInquiry>> getInquiryById(@PathVariable Long id) {
        return contactService.getInquiryById(id)
                .map(inquiry -> ResponseEntity.ok(ApiResponse.success("Inquiry retrieved successfully", inquiry)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 문의 상태 업데이트 (Admin only)
     */
    @PutMapping("/inquiries/{id}/status")
    public ResponseEntity<ApiResponse<ContactInquiry>> updateInquiryStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            ContactInquiry.InquiryStatus status = ContactInquiry.InquiryStatus.valueOf(request.get("status"));
            String adminNote = request.get("adminNote");

            ContactInquiry updated = contactService.updateInquiryStatus(id, status, adminNote);
            return ResponseEntity.ok(ApiResponse.success("Inquiry status updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update inquiry status: " + e.getMessage()));
        }
    }

    /**
     * 신규 문의 수 조회 (Admin only)
     */
    @GetMapping("/admin/new/count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getNewInquiriesCount() {
        Long count = contactService.getNewInquiriesCount();
        Map<String, Long> data = new HashMap<>();
        data.put("count", count);
        return ResponseEntity.ok(ApiResponse.success("New inquiries count retrieved", data));
    }

    /**
     * 문의 삭제 (Admin only)
     */
    @DeleteMapping("/inquiries/{id}")
    public ResponseEntity<ApiResponse<String>> deleteInquiry(@PathVariable Long id) {
        try {
            contactService.deleteInquiry(id);
            return ResponseEntity.ok(ApiResponse.success("Inquiry deleted successfully", "OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete inquiry: " + e.getMessage()));
        }
    }
}
