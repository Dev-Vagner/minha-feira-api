package br.com.vbruno.minhafeira.service.user.search;

import br.com.vbruno.minhafeira.domain.VerificationTokenPassword;
import br.com.vbruno.minhafeira.exception.TokenInvalidException;
import br.com.vbruno.minhafeira.factory.VerificationTokenPasswordFactory;
import br.com.vbruno.minhafeira.repository.VerificationTokenPasswordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchTokenRecoveryPasswordServiceTest {

    @InjectMocks
    private SearchTokenRecoveryPasswordService tested;

    @Mock
    private VerificationTokenPasswordRepository verificationTokenPasswordRepository;

    @Test
    @DisplayName("Deve retornar um VerificationTokenPassword quando o token passado for válido")
    void deveRetornarVerificationTokenPasswordQuandoTokenValido() {
        String tokenValid = "acbc31b6-19d4-4751-8fce-54dc4babd113";
        UUID tokenValidUUID = UUID.fromString(tokenValid);
        VerificationTokenPassword verification = VerificationTokenPasswordFactory.getVerificationTokenPassword();

        when(verificationTokenPasswordRepository.findByToken(tokenValidUUID)).thenReturn(Optional.of(verification));

        VerificationTokenPassword verificationReturned = tested.byToken(tokenValid);

        verify(verificationTokenPasswordRepository).findByToken(tokenValidUUID);

        assertEquals(verification, verificationReturned);
    }

    @Test
    @DisplayName("Deve retornar erro quando o formato do token passado for inválido")
    void deveRetornarErroQuandoFormatoTokenInvalido() {
        String tokenInvalid = "123";
        UUID tokenValidUUID = UUID.fromString("acbc31b6-19d4-4751-8fce-54dc4babd113");

        TokenInvalidException exception =
                assertThrows(TokenInvalidException.class, () -> tested.byToken(tokenInvalid));

        assertThrows(TokenInvalidException.class, () -> tested.byToken(tokenInvalid));

        verify(verificationTokenPasswordRepository, never()).findByToken(tokenValidUUID);

        assertEquals("Token inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando token passado não for encontrado no banco de dados")
    void deveRetornarErroQuandoTokenNaoEncontradoNoBancoDeDados() {
        String token = "acbc31b6-19d4-4751-8fce-54dc4babd113";
        UUID tokenUUID = UUID.fromString(token);

        doThrow(TokenInvalidException.class)
                .when(verificationTokenPasswordRepository).findByToken(tokenUUID);

        assertThrows(TokenInvalidException.class, () -> tested.byToken(token));

        verify(verificationTokenPasswordRepository).findByToken(tokenUUID);
    }

    @Test
    @DisplayName("Deve retornar erro quando token passado estiver expirado")
    void deveRetornarErroQuandoTokenExpirado() {
        String token = "acbc31b6-19d4-4751-8fce-54dc4babd113";
        UUID tokenUUID = UUID.fromString(token);
        VerificationTokenPassword verificationExpired = VerificationTokenPasswordFactory.getVerificationTokenPasswordExpired();

        when(verificationTokenPasswordRepository.findByToken(tokenUUID)).thenReturn(Optional.of(verificationExpired));

        assertThrows(TokenInvalidException.class, () -> tested.byToken(token));

        verify(verificationTokenPasswordRepository).findByToken(tokenUUID);
    }
}