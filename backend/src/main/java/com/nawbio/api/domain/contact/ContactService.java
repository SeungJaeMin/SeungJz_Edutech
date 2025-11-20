package com.nawbio.api.domain.contact;

import com.nawbio.api.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    private ContactInquiryRepository contactInquiryRepository;

    @Autowired
    private EmailService emailService;

    /**
     * 신규 문의 저장 및 이메일 알림 전송
     */
    @Transactional
    public ContactInquiry createInquiry(ContactRequest request) {
        // DB에 저장
        ContactInquiry inquiry = new ContactInquiry(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getSubject(),
                request.getMessage()
        );

        ContactInquiry savedInquiry = contactInquiryRepository.save(inquiry);
        logger.info("New contact inquiry saved: ID={}, Email={}", savedInquiry.getId(), savedInquiry.getEmail());

        // 관리자에게 알림 이메일 전송 (비동기로 처리되어 실패해도 저장은 완료됨)
        try {
            emailService.sendNewInquiryNotification(
                    request.getName(),
                    request.getEmail(),
                    request.getSubject(),
                    request.getMessage()
            );

            // 고객에게 확인 이메일 전송
            emailService.sendInquiryConfirmation(
                    request.getEmail(),
                    request.getName()
            );
        } catch (Exception e) {
            logger.error("Failed to send email notifications, but inquiry is saved: {}", e.getMessage());
        }

        return savedInquiry;
    }

    /**
     * 모든 문의 조회 (최신순)
     */
    public List<ContactInquiry> getAllInquiries() {
        return contactInquiryRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 상태별 문의 조회
     */
    public List<ContactInquiry> getInquiriesByStatus(ContactInquiry.InquiryStatus status) {
        return contactInquiryRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    /**
     * 문의 상세 조회
     */
    public Optional<ContactInquiry> getInquiryById(Long id) {
        return contactInquiryRepository.findById(id);
    }

    /**
     * 문의 상태 업데이트
     */
    @Transactional
    public ContactInquiry updateInquiryStatus(Long id, ContactInquiry.InquiryStatus status, String adminNote) {
        ContactInquiry inquiry = contactInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found: " + id));

        inquiry.setStatus(status);
        if (adminNote != null) {
            inquiry.setAdminNote(adminNote);
        }

        return contactInquiryRepository.save(inquiry);
    }

    /**
     * 신규 문의 수 조회
     */
    public Long getNewInquiriesCount() {
        return contactInquiryRepository.countNewInquiries();
    }

    /**
     * 문의 삭제
     */
    @Transactional
    public void deleteInquiry(Long id) {
        contactInquiryRepository.deleteById(id);
    }
}
