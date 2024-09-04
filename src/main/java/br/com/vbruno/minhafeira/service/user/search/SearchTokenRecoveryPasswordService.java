package br.com.vbruno.minhafeira.service.user.search;

import br.com.vbruno.minhafeira.domain.VerificationTokenPassword;
import br.com.vbruno.minhafeira.exception.TokenInvalidException;
import br.com.vbruno.minhafeira.repository.VerificationTokenPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SearchTokenRecoveryPasswordService {

    @Autowired
    private VerificationTokenPasswordRepository verificationTokenPasswordRepository;

    public VerificationTokenPassword byToken(String token) {
        UUID tokenFormatValidate = formatTokenValidate(token);

        VerificationTokenPassword verification = verificationTokenPasswordRepository.findByToken(tokenFormatValidate)
                .orElseThrow(TokenInvalidException::new);

        boolean tokenExpired = LocalDateTime.now().isAfter(verification.getDataExpiracao());
        if(tokenExpired) throw new TokenInvalidException();

        return verification;
    }

    private UUID formatTokenValidate(String token) {
        try {
            return UUID.fromString(token);
        } catch (IllegalArgumentException ex) {
            throw new TokenInvalidException();
        }
    }
}
