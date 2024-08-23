package br.com.vbruno.minhafeira.mapper.category;

import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DetailsCategoryMapperTest {

    @Test
    @DisplayName("Deve retornar um DetailsCategoryResponse quando passado uma entidade Category")
    void deveRetornarUmDetailsCategoryResponse() {
        Category category = CategoryFactory.getCategory();

        DetailsCategoryResponse detailsCategoryResponse = DetailsCategoryMapper.toResponse(category);

        Assertions.assertNotNull(detailsCategoryResponse);
        Assertions.assertEquals(category.getId(), detailsCategoryResponse.getId());
        Assertions.assertEquals(category.getName(), detailsCategoryResponse.getName());
    }
}