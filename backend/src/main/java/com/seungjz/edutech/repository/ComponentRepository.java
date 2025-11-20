package com.seungjz.edutech.repository;

import com.seungjz.edutech.domain.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    List<Component> findByLectureIdOrderByDisplayOrderAsc(Long lectureId);
}
