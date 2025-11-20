package com.seungjz.edutech.domain.lecture.repository;

import com.seungjz.edutech.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByLevel(Integer level);

    List<Lecture> findByType(Lecture.LectureType type);
}
