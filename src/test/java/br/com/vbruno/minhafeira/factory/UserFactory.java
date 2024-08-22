package br.com.vbruno.minhafeira.factory;

import br.com.vbruno.minhafeira.DTO.request.user.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.request.user.UpdateUserRequest;
import br.com.vbruno.minhafeira.domain.User;

public class UserFactory {

    public static User getUser() {
        return User.builder()
                .id(1L)
                .name("Nome teste")
                .email("teste@email.com")
                .password("teste123")
                .build();
    }

    public static CreateUserRequest getCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Request Teste");
        request.setEmail("requesttest@email.com");
        request.setPassword("requesttestpassword");

        return request;
    }

    public static UpdateUserRequest getUpdateUserRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Request edição teste");
        request.setEmail("requesteditteste@email.com");

        return request;
    }
}
