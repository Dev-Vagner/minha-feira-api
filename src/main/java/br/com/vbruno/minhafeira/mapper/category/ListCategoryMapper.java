package br.com.vbruno.minhafeira.mapper.category;

import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;

public class ListCategoryMapper {

    public static ListCategoryResponse toResponse(Category category) {
        return ListCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
