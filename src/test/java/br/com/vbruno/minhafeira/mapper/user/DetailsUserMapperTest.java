package br.com.vbruno.minhafeira.mapper.user;

import br.com.vbruno.minhafeira.DTO.response.user.DetailsUserResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.factory.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DetailsUserMapperTest {

    @Test
    @DisplayName("Deve retornar um DetailsUserResponse quando passado uma entidade User")
    void deveRetornarUmDetailsUserResponse() {
        User user = UserFactory.getUser();

        DetailsUserResponse detailsUserResponse = DetailsUserMapper.toResponse(user);

        Assertions.assertNotNull(detailsUserResponse);
        Assertions.assertEquals(user.getId(), detailsUserResponse.getId());
        Assertions.assertEquals(user.getName(), detailsUserResponse.getName());
        Assertions.assertEquals(user.getEmail(), detailsUserResponse.getEmail());
    }
}