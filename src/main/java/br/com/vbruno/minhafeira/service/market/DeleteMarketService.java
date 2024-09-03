package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteMarketService {

    @Autowired
    private SearchMarketFromUserService searchMarketFromUserService;

    @Autowired
    private MarketRepository marketRepository;

    @Transactional
    public void delete(Long idMarket) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        searchMarketFromUserService.byId(idMarket, user.getId());

        marketRepository.deleteById(idMarket);
    }
}
