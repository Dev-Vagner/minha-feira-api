package br.com.vbruno.minhafeira.mapper.market;

import br.com.vbruno.minhafeira.DTO.response.market.DetailsProductQuantityResponse;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DetailsProductQuantityMapperTest {

    @Test
    @DisplayName("Deve retornar um DetailsProductQuantityResponse sem valor de produto x quantidade, quando passado uma entidade ProductQuantity")
    void deveRetornarUmDetailsProductQuantityResponseSemValorProdutoQuantidade() {
        ProductQuantity productQuantity = MarketFactory.getProductQuantity();

        DetailsProductQuantityResponse detailsProductQuantityResponse = DetailsProductQuantityMapper.toResponse(productQuantity);

        Assertions.assertNotNull(detailsProductQuantityResponse);
        Assertions.assertEquals(productQuantity.getProduct(), detailsProductQuantityResponse.getProduct());
        Assertions.assertEquals(productQuantity.getQuantity(), detailsProductQuantityResponse.getQuantity());
        Assertions.assertEquals(productQuantity.getUnitValue(), detailsProductQuantityResponse.getUnitValue());
        Assertions.assertNull(detailsProductQuantityResponse.getProductQuantityValue());
    }
}