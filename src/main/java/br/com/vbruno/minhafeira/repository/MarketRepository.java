package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Optional<Market> findByIdAndUserId(Long marketId, Long userId);

    Page<Market> findAllByUserId(Long userId, Pageable pageable);
}
