package br.com.vbruno.minhafeira.DTO.response.product;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetailsProductResponse {
    private Long id;
    private String name;
    private Long idCategory;
    private String nameCategory;
}
