package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {
}
