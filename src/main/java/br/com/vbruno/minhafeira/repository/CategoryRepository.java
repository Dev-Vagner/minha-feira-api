package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCaseAndUserId(String nameCategory, Long userId);
}
