package br.com.vbruno.minhafeira.mapper.product;

import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DetailsProductMapperTest {

    @Test
    @DisplayName("Deve retornar um DetailsProductResponse com categoria quando passado uma entidade Product com categoria")
    void deveRetornarUmDetailsProductResponseComCategoria() {
        Product productComplete = ProductFactory.getProductWithCategory();

        DetailsProductResponse detailsProductResponse = DetailsProductMapper.toResponseCategoryNotNull(productComplete);

        Assertions.assertNotNull(detailsProductResponse);
        Assertions.assertEquals(productComplete.getId(), detailsProductResponse.getId());
        Assertions.assertEquals(productComplete.getName(), detailsProductResponse.getName());
        Assertions.assertEquals(productComplete.getCategory().getName(), detailsProductResponse.getCategory());
    }

    @Test
    @DisplayName("Deve retornar um DetailsProductResponse com categoria null quando passado uma entidade Product sem categoria")
    void deveRetornarUmDetailsProductResponseComCategoriaNull() {
        Product productNotCategory = ProductFactory.getProductNotCategory();

        DetailsProductResponse detailsProductResponse = DetailsProductMapper.toResponseBase(productNotCategory);

        Assertions.assertNotNull(detailsProductResponse);
        Assertions.assertEquals(productNotCategory.getId(), detailsProductResponse.getId());
        Assertions.assertEquals(productNotCategory.getName(), detailsProductResponse.getName());
        Assertions.assertNull(detailsProductResponse.getCategory());
    }
}