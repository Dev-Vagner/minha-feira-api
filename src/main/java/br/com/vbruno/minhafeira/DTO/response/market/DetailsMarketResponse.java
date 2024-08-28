package br.com.vbruno.minhafeira.DTO.response.market;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetailsMarketResponse {
    private Long id;
    private LocalDate dateMarket;
    private List<DetailsProductQuantityResponse> listProductsQuantities;
    private BigDecimal totalValue;
    private String observation;
}
