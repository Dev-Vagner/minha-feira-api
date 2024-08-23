package br.com.vbruno.minhafeira.service.user.validate;

import br.com.vbruno.minhafeira.exception.EmailRegisteredException;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateUniqueEmailUserServiceTest {

    @InjectMocks
    private ValidateUniqueEmailUserService tested;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Não deve fazer nada quando o email for único no sistema")
    void naoDeveFazerNadaQuandoEmailForUnico() {

        String email = "teste@email.com";
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);

        tested.validate(email);

        Mockito.verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("Deve retornar erro quando o email já estiver cadastrado no sistema")
    void deveRetornarErroQuandoEmailJaTiverCadastrado() {

        String email = "teste@email.com";
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

        EmailRegisteredException exception =
                Assertions.assertThrows(EmailRegisteredException.class, () -> tested.validate(email));

        Mockito.verify(userRepository).existsByEmail(email);

        Assertions.assertEquals("Este email já está cadastrado", exception.getMessage());
    }
}