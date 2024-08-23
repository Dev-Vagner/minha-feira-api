package br.com.vbruno.minhafeira.mapper.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.domain.Product;

public class ListProductMapper {

    public static ListProductResponse toResponse(Product product) {
        return ListProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .build();
    }
}
