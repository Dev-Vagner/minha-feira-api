package br.com.vbruno.minhafeira.service.user.search;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SearchUserServiceTest {

    @InjectMocks
    private SearchUserService tested;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve retornar usuário quando o ID do usuário for válido")
    void deveRetornarUsuarioQuandoIdValido() {
        User user = UserFactory.getUser();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User userReturned = tested.byId(user.getId());

        Mockito.verify(userRepository).findById(user.getId());

        Assertions.assertEquals(user, userReturned);
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID do usuário for inválido")
    void deveRetornarErroQuandoIdInvalido() {
        Long idUser = 1L;

        UserNotRegisteredException exception =
                    Assertions.assertThrows(UserNotRegisteredException.class, () -> tested.byId(idUser));

        Mockito.verify(userRepository).findById(idUser);

        Assertions.assertEquals("Usuário não encontrado", exception.getMessage());
    }
}