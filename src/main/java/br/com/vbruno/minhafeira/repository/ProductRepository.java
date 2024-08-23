package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCaseAndUserId(String productName, Long userId);

}
