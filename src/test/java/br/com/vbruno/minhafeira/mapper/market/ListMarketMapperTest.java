package br.com.vbruno.minhafeira.mapper.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ListMarketMapperTest {

    @Test
    @DisplayName("Deve retornar um ListMarketResponse quando passado uma entidade Market")
    void deveRetornarUmListMarketResponse() {
        Market market = MarketFactory.getMarket();

        ListMarketResponse listMarketResponse = ListMarketMapper.toResponse(market);

        Assertions.assertNotNull(listMarketResponse);
        Assertions.assertEquals(market.getId(), listMarketResponse.getId());
        Assertions.assertEquals(market.getDateMarket(), listMarketResponse.getDateMarket());
        Assertions.assertEquals(market.getTotalValue(), listMarketResponse.getTotalValue());
    }

}