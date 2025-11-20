package com.nawbio.api.service;

import com.nawbio.api.domain.settings.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SettingsService settingsService;

    /**
     * 관리자에게 신규 문의 알림 이메일 전송
     */
    public void sendNewInquiryNotification(String name, String email, String subject, String message) {
        try {
            // DB에서 관리자 이메일 리스트 조회
            List<String> adminEmails = settingsService.getAdminEmails();

            if (adminEmails == null || adminEmails.isEmpty()) {
                logger.warn("No admin emails configured. Skipping notification.");
                return;
            }

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(adminEmails.toArray(new String[0]));  // 여러 수신자
            mailMessage.setSubject("[NAWBIO] 새로운 문의가 도착했습니다");
            mailMessage.setText(
                    "새로운 문의가 접수되었습니다.\n\n" +
                    "보낸 사람: " + name + "\n" +
                    "이메일: " + email + "\n" +
                    "제목: " + (subject != null ? subject : "(제목 없음)") + "\n\n" +
                    "내용:\n" + message + "\n\n" +
                    "---\n" +
                    "관리자 대시보드에서 확인하세요: https://nawbio.com/admin/dashboard"
            );

            mailSender.send(mailMessage);
            logger.info("Admin notification emails sent to {} recipients for inquiry from: {}",
                    adminEmails.size(), email);

        } catch (Exception e) {
            logger.error("Failed to send admin notification email: {}", e.getMessage());
            // 이메일 전송 실패해도 문의는 저장되므로 예외를 던지지 않음
        }
    }

    /**
     * 고객에게 문의 접수 확인 이메일 전송
     */
    public void sendInquiryConfirmation(String customerEmail, String customerName) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(customerEmail);
            mailMessage.setSubject("[NAWBIO] 문의가 접수되었습니다");
            mailMessage.setText(
                    customerName + "님, 안녕하세요.\n\n" +
                    "나우바이오에 문의해 주셔서 감사합니다.\n" +
                    "접수하신 문의는 영업일 기준 1-2일 내에 답변드리겠습니다.\n\n" +
                    "감사합니다.\n" +
                    "나우바이오 드림"
            );

            mailSender.send(mailMessage);
            logger.info("Confirmation email sent to: {}", customerEmail);

        } catch (Exception e) {
            logger.error("Failed to send confirmation email: {}", e.getMessage());
            // 이메일 전송 실패해도 문의는 저장되므로 예외를 던지지 않음
        }
    }
}
