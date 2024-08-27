package br.com.vbruno.minhafeira.DTO.request.market;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMarketRequest {

    @NotNull(message = "É necessário passar uma lista de produtos e quantidades para cadastrar uma nova feira")
    private List<CreateProductQuantityRequest> listCreateProductQuantityRequest;
}
