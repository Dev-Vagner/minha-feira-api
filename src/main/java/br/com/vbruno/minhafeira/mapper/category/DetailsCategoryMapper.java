package br.com.vbruno.minhafeira.mapper.category;

import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;

public class DetailsCategoryMapper {

    public static DetailsCategoryResponse toResponse(Category category) {
        return DetailsCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
