package com.nawbio.api.domain.analytics;

import com.nawbio.api.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 자체 Analytics 컨트롤러
 * PostgreSQL 기반 페이지 방문 추적 및 통계
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * 페이지 방문 추적 (Public - 모든 방문자)
     */
    @PostMapping("/track")
    public ResponseEntity<ApiResponse<String>> trackPageView(
            @RequestBody TrackingRequest request,
            HttpServletRequest httpRequest) {
        analyticsService.trackPageView(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success("Page view tracked", "OK"));
    }

    /**
     * Analytics 통계 조회 (Admin only - 인증 필요)
     */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOverview() {
        Map<String, Object> data = analyticsService.getAnalyticsData();
        return ResponseEntity.ok(ApiResponse.success("Analytics overview", data));
    }

    /**
     * 실시간 Analytics (현재는 placeholder)
     */
    @GetMapping("/realtime")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRealtime() {
        Map<String, Object> data = new HashMap<>();
        data.put("activeUsers", 0);
        data.put("activePages", new ArrayList<>());
        return ResponseEntity.ok(ApiResponse.success("Realtime analytics", data));
    }
}
