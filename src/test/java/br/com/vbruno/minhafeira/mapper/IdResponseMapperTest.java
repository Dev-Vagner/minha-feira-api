package br.com.vbruno.minhafeira.mapper;

import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IdResponseMapperTest {

    @Test
    @DisplayName("Deve retornar um IdResponse quando passado um ID")
    public void deveRetornarUmUser() {
        Long id = 1L;

        IdResponse idResponse = IdResponseMapper.toResponse(id);

        Assertions.assertNotNull(idResponse);
        Assertions.assertEquals(id, idResponse.getId());
    }
}