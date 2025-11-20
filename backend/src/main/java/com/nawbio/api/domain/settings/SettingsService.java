package com.nawbio.api.domain.settings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingsService {
    private static final Logger logger = LoggerFactory.getLogger(SettingsService.class);
    private static final String ADMIN_EMAILS_KEY = "email.admin.addresses";

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 관리자 이메일 리스트 조회
     */
    public List<String> getAdminEmails() {
        try {
            SystemSetting setting = systemSettingRepository.findBySettingKey(ADMIN_EMAILS_KEY)
                    .orElse(null);

            if (setting == null || setting.getSettingValue() == null || setting.getSettingValue().isEmpty()) {
                return new ArrayList<>();
            }

            // JSON 배열을 List로 변환
            return objectMapper.readValue(setting.getSettingValue(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            logger.error("Failed to parse admin emails: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 관리자 이메일 리스트 업데이트
     */
    @Transactional
    public List<String> updateAdminEmails(List<String> emails) {
        try {
            // JSON 배열로 변환
            String jsonValue = objectMapper.writeValueAsString(emails);

            SystemSetting setting = systemSettingRepository.findBySettingKey(ADMIN_EMAILS_KEY)
                    .orElse(new SystemSetting(ADMIN_EMAILS_KEY, jsonValue, "관리자 이메일 주소 목록"));

            setting.setSettingValue(jsonValue);
            systemSettingRepository.save(setting);

            logger.info("Admin emails updated: {}", emails);
            return emails;
        } catch (Exception e) {
            logger.error("Failed to update admin emails: {}", e.getMessage());
            throw new RuntimeException("Failed to update admin emails", e);
        }
    }

    /**
     * 관리자 이메일 추가
     */
    @Transactional
    public List<String> addAdminEmail(String email) {
        List<String> emails = getAdminEmails();
        if (!emails.contains(email)) {
            emails.add(email);
            return updateAdminEmails(emails);
        }
        return emails;
    }

    /**
     * 관리자 이메일 삭제
     */
    @Transactional
    public List<String> removeAdminEmail(String email) {
        List<String> emails = getAdminEmails();
        emails.remove(email);
        return updateAdminEmails(emails);
    }
}
