package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.EmailRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.validate.ValidateUniqueEmailUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @InjectMocks
    private CreateUserService tested;

    @Mock
    private ValidateUniqueEmailUserService validateUniqueEmailUserService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso")
    void deveCadastrarNovoUsuario() {
        CreateUserRequest createUserRequest = UserFactory.getCreateUserRequest();

        IdResponse idResponse = tested.register(createUserRequest);

        Mockito.verify(validateUniqueEmailUserService).validate(createUserRequest.getEmail());
        Mockito.verify(userRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();

        Assertions.assertEquals(user.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar usuário com email já cadastrado no sistema")
    void deveRetornarErroQuandoEmailJaForCadastrado() {
        CreateUserRequest createUserRequest = UserFactory.getCreateUserRequest();

        Mockito.doThrow(EmailRegisteredException.class)
                .when(validateUniqueEmailUserService).validate(createUserRequest.getEmail());

        Assertions.assertThrows(EmailRegisteredException.class, () -> tested.register(createUserRequest));

        Mockito.verify(validateUniqueEmailUserService).validate(createUserRequest.getEmail());
        Mockito.verify(userRepository, Mockito.never()).save(userCaptor.capture());
    }
}