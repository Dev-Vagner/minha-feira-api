package br.com.vbruno.minhafeira.DTO.response.market;

import br.com.vbruno.minhafeira.domain.Product;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetailsProductQuantityResponse {
    private Product product;
    private Integer quantity;
    private BigDecimal unitValue;
    private BigDecimal productQuantityValue;
}
