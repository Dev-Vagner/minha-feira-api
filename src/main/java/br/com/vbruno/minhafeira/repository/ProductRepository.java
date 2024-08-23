package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCaseAndUserId(String productName, Long userId);

    Optional<Product> findByIdAndUserId(Long productId, Long userId);

}
