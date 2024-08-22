package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCaseAndUserId(String categoryName, Long userId);

    Optional<Category> findByIdAndUserId(Long categoryId, Long userId);

    List<Category> findAllByUserId(Long userId);
}
