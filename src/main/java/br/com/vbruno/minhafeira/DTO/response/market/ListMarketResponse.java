package br.com.vbruno.minhafeira.DTO.response.market;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListMarketResponse {
    private Long id;
    private LocalDate dateMarket;
    private BigDecimal totalValue;
}
