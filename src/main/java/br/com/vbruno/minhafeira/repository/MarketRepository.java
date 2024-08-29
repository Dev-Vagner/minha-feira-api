package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Optional<Market> findByIdAndUserId(Long marketId, Long userId);

    Page<Market> findAllByUserId(Long userId, Pageable pageable);

    Page<Market> findAllByUserIdAndDateMarketBetween(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
