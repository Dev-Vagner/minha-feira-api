package br.com.vbruno.minhafeira.mapper.user;

import br.com.vbruno.minhafeira.DTO.request.CreateUserRequest;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.factory.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateUserMapperTest {

    @Test
    @DisplayName("Deve retornar uma entidade User quando passado um CreateUserRequest")
    void deveRetornarUmUser() {
        CreateUserRequest createUserRequest = UserFactory.getCreateUserRequest();

        User user = CreateUserMapper.toEntity(createUserRequest);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(createUserRequest.getName(), user.getName());
        Assertions.assertEquals(createUserRequest.getEmail(), user.getEmail());
        Assertions.assertEquals(createUserRequest.getPassword(), user.getPassword());
    }
}