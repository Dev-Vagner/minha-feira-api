package br.com.vbruno.minhafeira.DTO.response.category;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListCategoryResponse {
    private Long id;
    private String name;
}
