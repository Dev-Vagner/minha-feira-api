package br.com.vbruno.minhafeira.DTO.response.product;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListProductResponse {
    private Long id;
    private String name;
}
