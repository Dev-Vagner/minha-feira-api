package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.mapper.market.ListMarketMapper;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListMarketService {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private MarketRepository marketRepository;

    public Page<ListMarketResponse> list(Long idUser, Pageable pageable) {
        searchUserService.byId(idUser);

        return marketRepository.findAllByUserId(idUser, pageable)
                .map(ListMarketMapper::toResponse);
    }
}
