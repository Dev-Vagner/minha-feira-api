package br.com.vbruno.minhafeira.mapper.market;

import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DetailsMarketMapperTest {

    @Test
    @DisplayName("Deve retornar um DetailsMarketResponse sem lista de produtos e quantidades quando passado uma entidade Market")
    void deveRetornarUmDetailsMarketResponseSemProdutoQuantidade() {
        Market market = MarketFactory.getMarket();

        DetailsMarketResponse detailsMarketResponse = DetailsMarketMapper.toResponse(market);

        Assertions.assertNotNull(detailsMarketResponse);
        Assertions.assertEquals(market.getId(), detailsMarketResponse.getId());
        Assertions.assertEquals(market.getDateMarket(), detailsMarketResponse.getDateMarket());
        Assertions.assertEquals(market.getTotalValue(), detailsMarketResponse.getTotalValue());
        Assertions.assertEquals(market.getObservation(), detailsMarketResponse.getObservation());
        Assertions.assertNull(detailsMarketResponse.getListProductsQuantities());
    }

}