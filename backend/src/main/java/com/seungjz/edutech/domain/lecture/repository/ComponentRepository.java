package com.seungjz.edutech.domain.lecture.repository;

import com.seungjz.edutech.domain.lecture.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    List<Component> findByLectureIdOrderByStartTimeAsc(Long lectureId);
}
