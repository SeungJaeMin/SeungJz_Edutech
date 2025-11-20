package com.nawbio.api.domain.analytics;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    @Autowired
    private PageViewRepository pageViewRepository;

    /**
     * 페이지 방문 기록
     */
    public void trackPageView(TrackingRequest request, HttpServletRequest httpRequest) {
        try {
            String ipAddress = getClientIpAddress(httpRequest);
            String visitorHash = hashIpAddress(ipAddress);
            String userAgent = httpRequest.getHeader("User-Agent");
            String deviceType = detectDeviceType(userAgent);
            String country = detectCountry(ipAddress);

            PageView pageView = new PageView(
                    request.getPagePath(),
                    visitorHash,
                    userAgent,
                    deviceType,
                    country,
                    request.getReferrer()
            );

            pageViewRepository.save(pageView);
            logger.info("Page view tracked: {} - Device: {} - Country: {}",
                    request.getPagePath(), deviceType, country);

        } catch (Exception e) {
            logger.error("Failed to track page view: {}", e.getMessage());
        }
    }

    /**
     * Analytics 통계 조회
     */
    public Map<String, Object> getAnalyticsData() {
        Map<String, Object> data = new HashMap<>();

        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime last30Days = LocalDateTime.now().minusDays(30);
        LocalDateTime last12Months = LocalDateTime.now().minusMonths(12);

        // 오늘 통계
        data.put("todayPageViews", pageViewRepository.countTodayPageViews(today));
        data.put("todayVisitors", pageViewRepository.countTodayUniqueVisitors(today));

        // 전체 통계
        data.put("totalPageViews", pageViewRepository.countTotalPageViews());
        data.put("totalVisitors", pageViewRepository.countTotalUniqueVisitors());

        // 인기 페이지 (최근 30일)
        List<Map<String, Object>> popularPages = pageViewRepository.findTopPages(last30Days);
        data.put("popularPages", formatPopularPages(popularPages));

        // 국가별 통계 (최근 30일)
        List<Map<String, Object>> countries = pageViewRepository.findTopCountries(last30Days);
        data.put("countriesData", formatCountriesData(countries));

        // 디바이스별 통계 (최근 30일)
        List<Map<String, Object>> devices = pageViewRepository.findDeviceStats(last30Days);
        data.put("devicesData", formatDevicesData(devices));

        // 일별 통계 (최근 30일)
        List<Map<String, Object>> dailyStats = pageViewRepository.findDailyStats(last30Days);
        data.put("dailyStats", formatDailyStats(dailyStats));

        // 월별 통계 (최근 12개월)
        List<Map<String, Object>> monthlyStats = pageViewRepository.findMonthlyStats(last12Months);
        data.put("monthlyStats", formatMonthlyStats(monthlyStats));

        return data;
    }

    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 쉼표로 구분된 경우 첫 번째 IP만 사용
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * IP 주소 해싱 (개인정보 보호)
     */
    private String hashIpAddress(String ip) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(ip.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to hash IP address", e);
            return "unknown";
        }
    }

    /**
     * 디바이스 타입 감지
     */
    private String detectDeviceType(String userAgent) {
        if (userAgent == null) return "unknown";

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("mobile") || userAgent.contains("android") ||
                userAgent.contains("iphone") || userAgent.contains("ipod")) {
            return "mobile";
        } else if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            return "tablet";
        } else {
            return "desktop";
        }
    }

    /**
     * 국가 감지 (간단한 구현 - 실제로는 GeoIP 라이브러리 사용 권장)
     */
    private String detectCountry(String ip) {
        // TODO: GeoIP 라이브러리 통합 (MaxMind GeoIP2 등)
        // 현재는 간단하게 처리
        if (ip == null) return "Unknown";

        if (ip.startsWith("127.") || ip.equals("0:0:0:0:0:0:0:1")) {
            return "Local";
        }

        // 한국 IP 범위 간단 체크 (매우 제한적)
        if (ip.startsWith("121.") || ip.startsWith("211.") ||
                ip.startsWith("114.") || ip.startsWith("210.")) {
            return "South Korea";
        }

        return "International";
    }

    /**
     * 인기 페이지 데이터 포맷팅
     */
    private List<Map<String, Object>> formatPopularPages(List<Map<String, Object>> rawData) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rawData) {
            Map<String, Object> page = new HashMap<>();
            page.put("path", row.get("pagepath"));
            page.put("views", ((Number) row.get("views")).intValue());
            result.add(page);
        }
        return result;
    }

    /**
     * 국가별 데이터 포맷팅
     */
    private List<Map<String, Object>> formatCountriesData(List<Map<String, Object>> rawData) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rawData) {
            Map<String, Object> country = new HashMap<>();
            country.put("country", row.get("country"));
            country.put("visitors", ((Number) row.get("visitors")).intValue());
            result.add(country);
        }
        return result;
    }

    /**
     * 디바이스별 데이터 포맷팅
     */
    private List<Map<String, Object>> formatDevicesData(List<Map<String, Object>> rawData) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rawData) {
            Map<String, Object> device = new HashMap<>();
            device.put("device", row.get("devicetype"));
            device.put("percentage", ((Number) row.get("percentage")).doubleValue());
            result.add(device);
        }
        return result;
    }

    /**
     * 일별 통계 데이터 포맷팅
     */
    private List<Map<String, Object>> formatDailyStats(List<Map<String, Object>> rawData) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rawData) {
            Map<String, Object> daily = new HashMap<>();
            daily.put("date", row.get("date").toString());
            daily.put("pageViews", ((Number) row.get("pageviews")).intValue());
            daily.put("visitors", ((Number) row.get("visitors")).intValue());
            result.add(daily);
        }
        return result;
    }

    /**
     * 월별 통계 데이터 포맷팅
     */
    private List<Map<String, Object>> formatMonthlyStats(List<Map<String, Object>> rawData) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rawData) {
            Map<String, Object> monthly = new HashMap<>();
            // month는 timestamp 형식으로 올 수 있으므로 문자열로 변환
            monthly.put("month", row.get("month").toString().substring(0, 7)); // YYYY-MM 형식
            monthly.put("pageViews", ((Number) row.get("pageviews")).intValue());
            monthly.put("visitors", ((Number) row.get("visitors")).intValue());
            result.add(monthly);
        }
        return result;
    }
}
