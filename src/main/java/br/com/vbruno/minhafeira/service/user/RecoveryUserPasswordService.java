package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.EmailRecoveryPasswordRequest;
import br.com.vbruno.minhafeira.DTO.request.user.RecoveryPasswordRequest;
import br.com.vbruno.minhafeira.DTO.response.MessageResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.domain.VerificationTokenPassword;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.repository.VerificationTokenPasswordRepository;
import br.com.vbruno.minhafeira.service.email.EmailService;
import br.com.vbruno.minhafeira.service.user.search.SearchTokenRecoveryPasswordService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RecoveryUserPasswordService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenPasswordRepository verificationTokenPasswordRepository;

    @Autowired
    private SearchTokenRecoveryPasswordService searchTokenRecoveryPasswordService;

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public MessageResponse sendEmail(EmailRecoveryPasswordRequest request) {
        User user = searchUserService.byEmail(request.getEmail());

        UUID token = UUID.randomUUID();
        LocalDateTime dataExpiracao = LocalDateTime.now().plusMinutes(15);

        verificationTokenPasswordRepository.deleteByUserId(user.getId());

        VerificationTokenPassword verificationTokenPassword = new VerificationTokenPassword();
        verificationTokenPassword.setToken(token);
        verificationTokenPassword.setDataExpiracao(dataExpiracao);
        verificationTokenPassword.setUser(user);
        verificationTokenPasswordRepository.save(verificationTokenPassword);

        String subjectEmail = "Recuperação de senha";
        String messageEmail = "Para você recuperar sua senha, utilize o token: " + token +
                " em até 15 minutos. Caso você gere um novo token, o token enviado neste email se tornará inválido!";

        emailService.sendEmail(request.getEmail(), subjectEmail, messageEmail);

        return new MessageResponse("O email, com o token de recuperação de senha, foi enviado com sucesso!");
    }

    @Transactional
    public MessageResponse recoveryPassword(RecoveryPasswordRequest request) {
        VerificationTokenPassword verification = searchTokenRecoveryPasswordService.byToken(request.getToken());

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.getNewPassword());

        User user = verification.getUser();
        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return new MessageResponse("Senha atualizada com sucesso!");
    }
}
