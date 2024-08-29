package br.com.vbruno.minhafeira.mapper.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;

public class ListMarketMapper {

    public static ListMarketResponse toResponse(Market market) {
        return ListMarketResponse.builder()
                .id(market.getId())
                .dateMarket(market.getDateMarket())
                .totalValue(market.getTotalValue())
                .build();
    }
}
