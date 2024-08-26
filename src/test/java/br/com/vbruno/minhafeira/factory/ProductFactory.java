package br.com.vbruno.minhafeira.factory;

import br.com.vbruno.minhafeira.DTO.request.product.CreateProductRequest;
import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;

public class ProductFactory {

    private static final User USER_TEST = UserFactory.getUser();
    private static final Category CATEGORY_TEST = CategoryFactory.getCategory();

    public static Product getProductNotCategory() {
        return Product.builder()
                .id(1L)
                .name("Produto teste")
                .user(USER_TEST)
                .build();
    }

    public static Product getProductWithCategory() {
        Product product = getProductNotCategory();
        product.setCategory(CATEGORY_TEST);

        return product;
    }

    public static Product getProductNotActive() {
        Product product = getProductWithCategory();
        product.setActive(false);

        return product;
    }

    public static CreateProductRequest getCreateProductRequestNotCategory() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Criação produto teste");

        return request;
    }

    public static CreateProductRequest getCreateProductRequestWithCategory() {
        CreateProductRequest request = getCreateProductRequestNotCategory();
        request.setCategoryId(1L);

        return request;
    }


    public static UpdateProductRequest getUpdateProductRequestNotCategory() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Edição produto teste");

        return request;
    }

    public static UpdateProductRequest getUpdateProductRequestWithCategory() {
        UpdateProductRequest request = getUpdateProductRequestNotCategory();
        request.setCategoryId(2L);

        return request;
    }
}
