package br.com.vbruno.minhafeira.DTO.request.market;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateMarketRequest {

    @NotNull(message = "É necessário passar a data da feira")
    private LocalDate dateMarket;

    @NotEmpty(message = "A lista de produtos e quantidades não pode está vazia")
    @Valid
    private List<UpdateProductQuantityRequest> listProductsQuantities;

    private BigDecimal totalValue;

    private String observation;
}
