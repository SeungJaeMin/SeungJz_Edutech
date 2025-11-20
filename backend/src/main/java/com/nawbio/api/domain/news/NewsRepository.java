package com.nawbio.api.domain.news;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @EntityGraph(attributePaths = {"translations"})
    List<News> findByIsPublishedTrueOrderByPublishedAtDesc();

    @EntityGraph(attributePaths = {"translations"})
    List<News> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"translations"})
    List<News> findByCategoryAndIsPublishedTrueOrderByPublishedAtDesc(String category);

    @EntityGraph(attributePaths = {"translations"})
    Optional<News> findById(Long id);
}
