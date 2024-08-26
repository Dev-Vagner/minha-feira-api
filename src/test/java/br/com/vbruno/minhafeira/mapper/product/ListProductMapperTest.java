package br.com.vbruno.minhafeira.mapper.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ListProductMapperTest {

    @Test
    @DisplayName("Deve retornar um ListProductResponse quando passado uma entidade Product")
    void deveRetornarUmListProductResponse() {
        Product product = ProductFactory.getProductWithCategory();

        ListProductResponse listProductResponse = ListProductMapper.toResponse(product);

        Assertions.assertNotNull(listProductResponse);
        Assertions.assertEquals(product.getId(), listProductResponse.getId());
        Assertions.assertEquals(product.getName(), listProductResponse.getName());
    }
}