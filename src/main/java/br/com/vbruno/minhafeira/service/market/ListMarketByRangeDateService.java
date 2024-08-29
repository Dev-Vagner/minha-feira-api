package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.mapper.market.ListMarketMapper;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.market.validate.ValidateRangeDateFromMarketService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ListMarketByRangeDateService {

    @Autowired
    private ValidateRangeDateFromMarketService validateRangeDateFromMarketService;

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private MarketRepository marketRepository;

    public Page<ListMarketResponse> listByRangeDate(Long idUser, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        searchUserService.byId(idUser);

        validateRangeDateFromMarketService.validate(startDate, endDate);

        return marketRepository.findAllByUserIdAndDateMarketBetween(idUser, startDate, endDate, pageable)
                .map(ListMarketMapper::toResponse);
    }
}
