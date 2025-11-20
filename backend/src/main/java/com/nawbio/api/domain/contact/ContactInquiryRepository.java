package com.nawbio.api.domain.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {

    // 상태별 조회
    List<ContactInquiry> findByStatusOrderByCreatedAtDesc(ContactInquiry.InquiryStatus status);

    // 모든 문의 조회 (최신순)
    List<ContactInquiry> findAllByOrderByCreatedAtDesc();

    // 신규 문의 수 조회
    @Query("SELECT COUNT(c) FROM ContactInquiry c WHERE c.status = 'NEW'")
    Long countNewInquiries();
}
