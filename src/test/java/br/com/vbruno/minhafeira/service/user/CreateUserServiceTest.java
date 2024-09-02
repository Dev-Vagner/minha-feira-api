package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.EmailRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.validate.ValidateUniqueEmailUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

        verify(validateUniqueEmailUserService).validate(createUserRequest.getEmail());
        verify(userRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();

        assertEquals(user.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar usuário com email já cadastrado no sistema")
    void deveRetornarErroQuandoEmailJaForCadastrado() {
        CreateUserRequest createUserRequest = UserFactory.getCreateUserRequest();

        doThrow(EmailRegisteredException.class)
                .when(validateUniqueEmailUserService).validate(createUserRequest.getEmail());

        assertThrows(EmailRegisteredException.class, () -> tested.register(createUserRequest));

        verify(validateUniqueEmailUserService).validate(createUserRequest.getEmail());
        verify(userRepository, never()).save(userCaptor.capture());
    }
}