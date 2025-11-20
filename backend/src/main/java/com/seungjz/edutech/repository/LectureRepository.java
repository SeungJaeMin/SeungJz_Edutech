package com.seungjz.edutech.repository;

import com.seungjz.edutech.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByTypeOrderByLevelAsc(Lecture.LectureType type);

    List<Lecture> findByLevelOrderByCreatedAtDesc(Integer level);

    @Query("SELECT l FROM Lecture l LEFT JOIN FETCH l.components WHERE l.id = :id")
    Optional<Lecture> findByIdWithComponents(Long id);
}
