package com.nawbio.api.domain.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"translations"})
    List<Product> findByIsActiveTrueOrderByDisplayOrder();

    @EntityGraph(attributePaths = {"translations"})
    List<Product> findByCategoryAndIsActiveTrueOrderByDisplayOrder(String category);

    @EntityGraph(attributePaths = {"translations"})
    Optional<Product> findById(Long id);
}
