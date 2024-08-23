package br.com.vbruno.minhafeira.factory;

import br.com.vbruno.minhafeira.DTO.request.category.CreateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;

public class CategoryFactory {

    private static final User USER_TEST = UserFactory.getUser();

    public static Category getCategory() {
        return Category.builder()
                .id(1L)
                .name("Categoria teste")
                .user(USER_TEST)
                .build();
    }

    public static CreateCategoryRequest getCreateCategoryRequest() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Criação Categoria teste");

        return request;
    }

    public static UpdateCategoryRequest getUpdateCategoryRequest() {
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setName("Edição Categoria teste");

        return request;
    }
}
