package br.com.vbruno.minhafeira.DTO.response.market;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetailsProductQuantityResponse {
    private String product;
    private Integer quantity;
    private BigDecimal unitValue;
    private BigDecimal totalValue;
}
