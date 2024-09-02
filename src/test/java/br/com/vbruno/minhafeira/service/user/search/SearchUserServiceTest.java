package br.com.vbruno.minhafeira.service.user.search;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User userReturned = tested.byId(user.getId());

        verify(userRepository).findById(user.getId());

        assertEquals(user, userReturned);
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID do usuário for inválido")
    void deveRetornarErroQuandoIdInvalido() {
        Long idUser = 1L;

        UserNotRegisteredException exception =
                    assertThrows(UserNotRegisteredException.class, () -> tested.byId(idUser));

        verify(userRepository).findById(idUser);

        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}