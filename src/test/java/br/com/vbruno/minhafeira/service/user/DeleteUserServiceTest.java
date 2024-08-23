package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserServiceTest {

    @InjectMocks
    private DeleteUserService tested;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve deletar o usuario com sucesso")
    void deveDeletarUsuario() {
        Long id = 1L;

        tested.delete(id);

        Mockito.verify(searchUserService).byId(id);
        Mockito.verify(userRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar deletar o usuario mas o ID for inválido")
    void deveRetornarErroQuandoIdInvalido() {
        Long id = 1L;

        Mockito.doThrow(UserNotRegisteredException.class)
                .when(searchUserService).byId(id);

        Assertions.assertThrows(UserNotRegisteredException.class, () -> tested.delete(id));

        Mockito.verify(searchUserService).byId(id);
        Mockito.verify(userRepository, Mockito.never()).deleteById(id);
    }
}