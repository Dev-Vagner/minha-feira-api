package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.EmailRecoveryPasswordRequest;
import br.com.vbruno.minhafeira.DTO.request.user.RecoveryPasswordRequest;
import br.com.vbruno.minhafeira.DTO.response.MessageResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.domain.VerificationTokenPassword;
import br.com.vbruno.minhafeira.exception.TokenInvalidException;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.factory.VerificationTokenPasswordFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.repository.VerificationTokenPasswordRepository;
import br.com.vbruno.minhafeira.service.email.EmailService;
import br.com.vbruno.minhafeira.service.user.search.SearchTokenRecoveryPasswordService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecoveryUserPasswordServiceTest {

    @InjectMocks
    private RecoveryUserPasswordService tested;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationTokenPasswordRepository verificationTokenPasswordRepository;

    @Mock
    private SearchTokenRecoveryPasswordService searchTokenRecoveryPasswordService;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<VerificationTokenPassword> verificationTokenPasswordCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    @DisplayName("Deve enviar email com token de recuperação de senha com sucesso")
    void deveEnviarEmailDeRecuperacaoDeSenha() {
        EmailRecoveryPasswordRequest request = new EmailRecoveryPasswordRequest("emailvalidotest@gmail.com");
        User user = UserFactory.getUser();

        when(searchUserService.byEmail(request.getEmail())).thenReturn(user);

        MessageResponse messageResponse = tested.sendEmail(request);

        verify(searchUserService).byEmail(request.getEmail());
        verify(verificationTokenPasswordRepository).deleteByUserId(user.getId());
        verify(verificationTokenPasswordRepository).save(verificationTokenPasswordCaptor.capture());

        assertEquals("O email, com o token de recuperação de senha, foi enviado com sucesso!",
                messageResponse.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando email passado não tiver cadastrado no banco de dados")
    void deveRetornarErroQuandoEmailInvalido() {
        EmailRecoveryPasswordRequest request = new EmailRecoveryPasswordRequest("emailvalidotest@gmail.com");
        Long idUser = 1L;

        doThrow(UserNotRegisteredException.class)
                .when(searchUserService).byEmail(request.getEmail());

        assertThrows(UserNotRegisteredException.class, () -> tested.sendEmail(request));

        verify(searchUserService).byEmail(request.getEmail());
        verify(verificationTokenPasswordRepository, never()).deleteByUserId(idUser);
        verify(verificationTokenPasswordRepository, never()).save(verificationTokenPasswordCaptor.capture());
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void deveAtualizarSenha() {
        RecoveryPasswordRequest request = new RecoveryPasswordRequest("acbc31b6-19d4-4751-8fce-54dc4babd113",
                "123456");
        VerificationTokenPassword verification = VerificationTokenPasswordFactory.getVerificationTokenPassword();

        when(searchTokenRecoveryPasswordService.byToken(request.getToken())).thenReturn(verification);

        MessageResponse messageResponse = tested.recoveryPassword(request);

        verify(searchTokenRecoveryPasswordService).byToken(request.getToken());
        verify(userRepository).save(userCaptor.capture());

        assertEquals("Senha atualizada com sucesso!", messageResponse.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando token enviado for inválido")
    void deveRetornarErroQuandoTokenInvalido() {
        RecoveryPasswordRequest request = new RecoveryPasswordRequest("acbc31b6-19d4-4751-8fce-54dc4babd113",
                "123456");

        doThrow(TokenInvalidException.class)
                .when(searchTokenRecoveryPasswordService).byToken(request.getToken());

        assertThrows(TokenInvalidException.class, () -> tested.recoveryPassword(request));

        verify(searchTokenRecoveryPasswordService).byToken(request.getToken());
        verify(userRepository, never()).save(userCaptor.capture());
    }
}