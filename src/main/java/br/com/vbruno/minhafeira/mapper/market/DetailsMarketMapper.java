package br.com.vbruno.minhafeira.mapper.market;

import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;

public class DetailsMarketMapper {

    public static DetailsMarketResponse toResponse(Market market) {
        return DetailsMarketResponse.builder()
                .id(market.getId())
                .dateMarket(market.getDateMarket())
                .totalValue(market.getTotalValue())
                .observation(market.getObservation())
                .build();
    }
}
