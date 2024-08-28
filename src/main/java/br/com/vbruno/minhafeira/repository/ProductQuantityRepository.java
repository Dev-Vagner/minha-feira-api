package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {

    void deleteAllByMarketId(Long marketId);
}
