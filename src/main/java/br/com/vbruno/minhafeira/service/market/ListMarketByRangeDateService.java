package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.market.ListMarketMapper;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.market.validate.ValidateRangeDateFromMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ListMarketByRangeDateService {

    @Autowired
    private ValidateRangeDateFromMarketService validateRangeDateFromMarketService;

    @Autowired
    private MarketRepository marketRepository;

    public Page<ListMarketResponse> listByRangeDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        validateRangeDateFromMarketService.validate(startDate, endDate);

        return marketRepository.findAllByUserIdAndDateMarketBetween(user.getId(), startDate, endDate, pageable)
                .map(ListMarketMapper::toResponse);
    }
}
