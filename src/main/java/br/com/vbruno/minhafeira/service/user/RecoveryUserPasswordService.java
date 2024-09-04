package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.EmailRecoveryPasswordRequest;
import br.com.vbruno.minhafeira.DTO.response.MessageResponse;
import br.com.vbruno.minhafeira.exception.EmailNotSentException;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecoveryUserPasswordService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public MessageResponse sendEmail(EmailRecoveryPasswordRequest request) {
        boolean emailExists = userRepository.existsByEmail(request.getEmail());
        if(!emailExists) throw new UserNotRegisteredException("Email não cadastrado");

        String subjectEmail = "Recuperação de senha";
        String messageEmail = "Para você recuperar sua senha, utilize o token: ";

        boolean emailSent = emailService.sendEmail(request.getEmail(), subjectEmail, messageEmail);
        if(!emailSent) throw new EmailNotSentException("Ocorreu um problema interno ao tentar enviar o email de recuperação de senha");

        return new MessageResponse("O email, com o token de recuperação de senha, foi enviado com sucesso!");
    }
}
