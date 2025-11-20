package com.nawbio.api.domain.analytics;

import com.nawbio.api.config.VercelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class VercelAnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(VercelAnalyticsService.class);

    @Autowired
    private VercelConfig vercelConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Vercel 프로젝트 목록 조회
     */
    public Map<String, Object> getProjects() {
        try {
            String url = vercelConfig.getApiBaseUrl() + "/v9/projects";

            if (vercelConfig.getTeamId() != null && !vercelConfig.getTeamId().isEmpty()) {
                url = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("teamId", vercelConfig.getTeamId())
                        .toUriString();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(vercelConfig.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            logger.info("Vercel projects fetched successfully");
            return response.getBody();
        } catch (Exception e) {
            logger.error("Failed to fetch Vercel projects: {}", e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }

    /**
     * Analytics 데이터 조회 (샘플 구현)
     * Vercel Analytics API는 현재 제한적이므로, 프로젝트 정보를 기반으로 샘플 데이터 반환
     */
    public Map<String, Object> getAnalyticsData() {
        // 먼저 프로젝트 목록을 가져와서 유효한 토큰인지 확인
        Map<String, Object> projects = getProjects();

        if (projects.containsKey("error")) {
            logger.error("Token validation failed");
            return getSampleData();
        }

        logger.info("Using Vercel token successfully");

        // TODO: Vercel Web Analytics API 연동
        // 현재 Vercel의 Web Analytics API는 Enterprise 플랜에서만 사용 가능
        // 대신 프론트엔드의 @vercel/analytics가 자동으로 데이터를 수집하고
        // Vercel 대시보드에서 확인 가능

        return getSampleData();
    }

    private Map<String, Object> getSampleData() {
        Map<String, Object> data = new HashMap<>();
        data.put("todayVisitors", 127);
        data.put("todayPageViews", 342);
        data.put("totalVisitors", 5420);
        data.put("totalPageViews", 18750);

        List<Map<String, Object>> popularPages = new ArrayList<>();
        popularPages.add(createPageData("/", 1250));
        popularPages.add(createPageData("/products", 820));
        popularPages.add(createPageData("/about", 540));
        popularPages.add(createPageData("/news", 380));
        popularPages.add(createPageData("/products/1", 210));
        data.put("popularPages", popularPages);

        List<Map<String, Object>> countriesData = new ArrayList<>();
        countriesData.add(createCountryData("South Korea", 3850));
        countriesData.add(createCountryData("United States", 720));
        countriesData.add(createCountryData("Japan", 420));
        countriesData.add(createCountryData("China", 280));
        countriesData.add(createCountryData("Others", 150));
        data.put("countriesData", countriesData);

        List<Map<String, Object>> devicesData = new ArrayList<>();
        devicesData.add(createDeviceData("Mobile", 62.5));
        devicesData.add(createDeviceData("Desktop", 32.1));
        devicesData.add(createDeviceData("Tablet", 5.4));
        data.put("devicesData", devicesData);

        return data;
    }

    private Map<String, Object> createPageData(String path, int views) {
        Map<String, Object> page = new HashMap<>();
        page.put("path", path);
        page.put("views", views);
        return page;
    }

    private Map<String, Object> createCountryData(String country, int visitors) {
        Map<String, Object> countryData = new HashMap<>();
        countryData.put("country", country);
        countryData.put("visitors", visitors);
        return countryData;
    }

    private Map<String, Object> createDeviceData(String device, double percentage) {
        Map<String, Object> deviceData = new HashMap<>();
        deviceData.put("device", device);
        deviceData.put("percentage", percentage);
        return deviceData;
    }
}
