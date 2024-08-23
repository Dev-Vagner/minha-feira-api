package br.com.vbruno.minhafeira.mapper.category;

import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ListCategoryMapperTest {

    @Test
    @DisplayName("Deve retornar um ListCategoryResponse quando passado uma entidade Category")
    void deveRetornarUmListCategoryResponse() {
        Category category = CategoryFactory.getCategory();

        ListCategoryResponse listCategoryResponse = ListCategoryMapper.toResponse(category);

        Assertions.assertNotNull(listCategoryResponse);
        Assertions.assertEquals(category.getId(), listCategoryResponse.getId());
        Assertions.assertEquals(category.getName(), listCategoryResponse.getName());
    }
}