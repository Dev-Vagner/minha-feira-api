package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.market.ListMarketMapper;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ListMarketService {

    @Autowired
    private MarketRepository marketRepository;

    public Page<ListMarketResponse> list(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return marketRepository.findAllByUserId(user.getId(), pageable)
                .map(ListMarketMapper::toResponse);
    }
}
