package com.nawbio.api.domain.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface PageViewRepository extends JpaRepository<PageView, Long> {

    // 오늘 페이지뷰 수
    @Query("SELECT COUNT(p) FROM PageView p WHERE p.createdAt >= :startDate")
    Long countTodayPageViews(@Param("startDate") LocalDateTime startDate);

    // 오늘 방문자 수 (유니크 visitor_hash)
    @Query("SELECT COUNT(DISTINCT p.visitorHash) FROM PageView p WHERE p.createdAt >= :startDate")
    Long countTodayUniqueVisitors(@Param("startDate") LocalDateTime startDate);

    // 전체 페이지뷰 수
    @Query("SELECT COUNT(p) FROM PageView p")
    Long countTotalPageViews();

    // 전체 방문자 수
    @Query("SELECT COUNT(DISTINCT p.visitorHash) FROM PageView p")
    Long countTotalUniqueVisitors();

    // 인기 페이지 TOP 5
    @Query(value = "SELECT page_path as pagePath, COUNT(*) as views " +
                   "FROM page_views " +
                   "WHERE created_at >= :startDate " +
                   "GROUP BY page_path " +
                   "ORDER BY views DESC " +
                   "LIMIT 5", nativeQuery = true)
    List<Map<String, Object>> findTopPages(@Param("startDate") LocalDateTime startDate);

    // 국가별 방문자 수 TOP 5
    @Query(value = "SELECT country, COUNT(DISTINCT visitor_hash) as visitors " +
                   "FROM page_views " +
                   "WHERE created_at >= :startDate AND country IS NOT NULL " +
                   "GROUP BY country " +
                   "ORDER BY visitors DESC " +
                   "LIMIT 5", nativeQuery = true)
    List<Map<String, Object>> findTopCountries(@Param("startDate") LocalDateTime startDate);

    // 디바이스별 통계
    @Query(value = "SELECT device_type as deviceType, " +
                   "COUNT(*) as count, " +
                   "ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM page_views WHERE created_at >= :startDate), 2) as percentage " +
                   "FROM page_views " +
                   "WHERE created_at >= :startDate AND device_type IS NOT NULL " +
                   "GROUP BY device_type " +
                   "ORDER BY count DESC", nativeQuery = true)
    List<Map<String, Object>> findDeviceStats(@Param("startDate") LocalDateTime startDate);

    // 일별 통계 (최근 30일)
    @Query(value = "SELECT " +
                   "DATE(created_at) as date, " +
                   "COUNT(*) as pageViews, " +
                   "COUNT(DISTINCT visitor_hash) as visitors " +
                   "FROM page_views " +
                   "WHERE created_at >= :startDate " +
                   "GROUP BY DATE(created_at) " +
                   "ORDER BY date ASC", nativeQuery = true)
    List<Map<String, Object>> findDailyStats(@Param("startDate") LocalDateTime startDate);

    // 월별 통계 (최근 12개월)
    @Query(value = "SELECT " +
                   "DATE_TRUNC('month', created_at) as month, " +
                   "COUNT(*) as pageViews, " +
                   "COUNT(DISTINCT visitor_hash) as visitors " +
                   "FROM page_views " +
                   "WHERE created_at >= :startDate " +
                   "GROUP BY DATE_TRUNC('month', created_at) " +
                   "ORDER BY month ASC", nativeQuery = true)
    List<Map<String, Object>> findMonthlyStats(@Param("startDate") LocalDateTime startDate);
}
