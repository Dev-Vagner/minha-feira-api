package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCaseAndUserIdAndActiveTrue(String productName, Long userId);

    Optional<Product> findByIdAndUserIdAndActiveTrue(Long productId, Long userId);

    List<Product> findAllByUserIdAndActiveTrue(Long userId);
}
