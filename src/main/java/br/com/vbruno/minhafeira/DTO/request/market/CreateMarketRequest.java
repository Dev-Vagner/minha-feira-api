package br.com.vbruno.minhafeira.DTO.request.market;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateMarketRequest {

    @NotEmpty(message = "A lista de produtos e quantidades não pode está vazia")

    @Valid
    private List<CreateProductQuantityRequest> listProductsQuantities;
}
