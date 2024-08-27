package br.com.vbruno.minhafeira.DTO.request.market;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductQuantityRequest {

    @NotNull(message = "É necessário passar o ID do produto na lista da feira")
    private Long productId;

    @NotNull(message = "É necessário passar a quantidade do produto na lista da feira")
    private Integer quantity;
}
