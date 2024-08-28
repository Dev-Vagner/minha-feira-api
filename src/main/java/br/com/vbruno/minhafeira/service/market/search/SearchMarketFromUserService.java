package br.com.vbruno.minhafeira.service.market.search;

import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchMarketFromUserService {

    @Autowired
    private MarketRepository marketRepository;

    public Market byId(Long marketId, Long userId) {
        return marketRepository.findByIdAndUserId(marketId, userId)
                .orElseThrow(() -> new MarketInvalidException("Feira inválida"));
    }
}
