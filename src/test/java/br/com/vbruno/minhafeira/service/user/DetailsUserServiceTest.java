package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.UpdateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.DetailsUserResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DetailsUserServiceTest {

    @InjectMocks
    private DetailsUserService tested;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve retornar os dados detalhados do usuario com sucesso")
    void deveRetornarDadosDetalhadosUsuario() {
        Long id = 1L;
        User user = UserFactory.getUser();

        Mockito.when(searchUserService.byId(id)).thenReturn(user);

        DetailsUserResponse detailsUserResponse = tested.details(id);

        Mockito.verify(searchUserService).byId(id);

        Assertions.assertEquals(user.getId(), detailsUserResponse.getId());
        Assertions.assertEquals(user.getName(), detailsUserResponse.getName());
        Assertions.assertEquals(user.getEmail(), detailsUserResponse.getEmail());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar retornar os dados do usuário mas o ID enviado for inválido")
    void deveRetornarErroQuandoIdInvalido() {
        Long id = 1L;

        Mockito.doThrow(UserNotRegisteredException.class)
                .when(searchUserService).byId(id);

        Assertions.assertThrows(UserNotRegisteredException.class, () -> tested.details(id));

        Mockito.verify(searchUserService).byId(id);
    }
}