package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.CreateUserRequest;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.validate.ValidateUniqueEmailUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @InjectMocks
    private CreateUserService createUserService;

    @Mock
    private ValidateUniqueEmailUserService validateUniqueEmailUserService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso")
    public void deveCadastrarNovoUsuarioComSucesso() {
        CreateUserRequest request = UserFactory.getCreateUserRequest();
        User user = UserFactory.getUser();

        Mockito.verify(validateUniqueEmailUserService).validate(request.getEmail());
        Mockito.verify(userRepository).save(user);

    }

    @Test
    @DisplayName("Não deve cadastrar um novo usuário e deve retornar erro quando email já for cadastrado")
    public void deveRetornarErroQuandoEmailJaForCadastrado() {

    }
}