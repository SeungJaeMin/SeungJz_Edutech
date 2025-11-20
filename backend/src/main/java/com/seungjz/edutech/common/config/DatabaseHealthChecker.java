package com.seungjz.edutech.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseHealthChecker implements CommandLineRunner {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        checkDatabaseConnection();
    }

    private void checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            log.info("=================================================");
            log.info("Database Connection Successful!");
            log.info("=================================================");
            log.info("Database Product: {}", metaData.getDatabaseProductName());
            log.info("Database Version: {}", metaData.getDatabaseProductVersion());
            log.info("Driver Name: {}", metaData.getDriverName());
            log.info("Driver Version: {}", metaData.getDriverVersion());
            log.info("URL: {}", metaData.getURL());
            log.info("Username: {}", metaData.getUserName());
            log.info("=================================================");

            // 간단한 쿼리 실행 테스트
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            log.info("Database Query Test: SUCCESS (result={})", result);

            // H2인 경우 추가 정보
            if (metaData.getDatabaseProductName().contains("H2")) {
                log.info("💡 H2 Database detected - In-memory mode");
                log.info("💡 H2 Console: http://localhost:8080/h2-console");
            }

            // PostgreSQL인 경우 추가 정보
            if (metaData.getDatabaseProductName().contains("PostgreSQL")) {
                log.info("🐘 PostgreSQL detected");
                String currentDatabase = jdbcTemplate.queryForObject(
                        "SELECT current_database()", String.class);
                log.info("Current Database: {}", currentDatabase);
            }

            log.info("=================================================");

        } catch (Exception e) {
            log.error("=================================================");
            log.error("❌ Database Connection Failed!");
            log.error("=================================================");
            log.error("Error Message: {}", e.getMessage());
            log.error("Please check your database configuration in application.yml");
            log.error("=================================================");
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
