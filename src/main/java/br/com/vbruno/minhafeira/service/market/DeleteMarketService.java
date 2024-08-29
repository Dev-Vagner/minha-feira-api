package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteMarketService {

    @Autowired
    private SearchMarketFromUserService searchMarketFromUserService;

    @Autowired
    private MarketRepository marketRepository;

    public void delete(Long idMarket, Long idUser) {
        searchMarketFromUserService.byId(idMarket, idUser);

        marketRepository.deleteById(idMarket);
    }
}
