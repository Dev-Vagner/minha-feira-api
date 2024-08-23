package br.com.vbruno.minhafeira.mapper.product;

import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.domain.Product;

public class DetailsProductMapper {

    public static DetailsProductResponse toResponseBase(Product product) {
        return DetailsProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .build();
    }

    public static DetailsProductResponse toResponseCategoryNotNull(Product product) {
        DetailsProductResponse detailsProductResponse = toResponseBase(product);

        detailsProductResponse.setIdCategory(product.getCategory().getId());
        detailsProductResponse.setNameCategory(product.getCategory().getName());

        return detailsProductResponse;
    }
}
