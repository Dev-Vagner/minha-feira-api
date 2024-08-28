package br.com.vbruno.minhafeira.mapper.market;

import br.com.vbruno.minhafeira.DTO.response.market.DetailsProductQuantityResponse;
import br.com.vbruno.minhafeira.domain.ProductQuantity;

public class DetailsProductQuantityMapper {

    public static DetailsProductQuantityResponse toResponse(ProductQuantity productQuantity) {
        return DetailsProductQuantityResponse.builder()
                .product(productQuantity.getProduct().getName())
                .quantity(productQuantity.getQuantity())
                .unitValue(productQuantity.getUnitValue())
                .build();
    }
}
