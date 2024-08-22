package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.UpdateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.EmailRegisteredException;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import br.com.vbruno.minhafeira.service.user.validate.ValidateUniqueEmailUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @InjectMocks
    private UpdateUserService tested;

    @Mock
    private ValidateUniqueEmailUserService validateUniqueEmailUserService;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Deve editar os dados do usuário com sucesso")
    void deveEditarDadosUsuario() {
        Long id = 1L;
        UpdateUserRequest updateUserRequest = UserFactory.getUpdateUserRequest();
        User user = UserFactory.getUser();

        Mockito.when(searchUserService.byId(id)).thenReturn(user);

        IdResponse idResponse = tested.update(id, updateUserRequest);

        Mockito.verify(searchUserService).byId(id);
        Mockito.verify(validateUniqueEmailUserService).validate(updateUserRequest.getEmail());
        Mockito.verify(userRepository).save(userCaptor.capture());

        User userSaved = userCaptor.getValue();

        Assertions.assertEquals(userSaved.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar usuário mas o ID enviado for inválido")
    void deveRetornarErroQuandoIdInvalido() {
        Long id = 1L;
        UpdateUserRequest updateUserRequest = UserFactory.getUpdateUserRequest();

        Mockito.doThrow(UserNotRegisteredException.class)
                .when(searchUserService).byId(id);

        Assertions.assertThrows(UserNotRegisteredException.class, () -> tested.update(id, updateUserRequest));

        Mockito.verify(searchUserService).byId(id);
        Mockito.verify(validateUniqueEmailUserService, Mockito.never()).validate(updateUserRequest.getEmail());
        Mockito.verify(userRepository, Mockito.never()).save(userCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar alterar email e o email já estiver cadastrado no sistema")
    void deveRetornarErroQuandoEmailJaCadastradoSistema() {
        Long id = 1L;
        UpdateUserRequest updateUserRequest = UserFactory.getUpdateUserRequest();
        User user = UserFactory.getUser();

        Mockito.when(searchUserService.byId(id)).thenReturn(user);

        Mockito.doThrow(EmailRegisteredException.class)
                .when(validateUniqueEmailUserService).validate(updateUserRequest.getEmail());

        Assertions.assertThrows(EmailRegisteredException.class, () -> tested.update(id, updateUserRequest));

        Mockito.verify(searchUserService).byId(id);
        Mockito.verify(validateUniqueEmailUserService).validate(updateUserRequest.getEmail());
        Mockito.verify(userRepository, Mockito.never()).save(userCaptor.capture());
    }
}