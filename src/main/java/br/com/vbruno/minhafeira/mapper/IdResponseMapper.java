package br.com.vbruno.minhafeira.mapper;

import br.com.vbruno.minhafeira.DTO.response.IdResponse;

public class IdResponseMapper {

    public static IdResponse toResponse(Long id) {
        return IdResponse.builder()
                .id(id)
                .build();
    }
}
