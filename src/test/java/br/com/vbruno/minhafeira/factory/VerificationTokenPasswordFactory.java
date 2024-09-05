package br.com.vbruno.minhafeira.factory;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.domain.VerificationTokenPassword;

import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationTokenPasswordFactory {

    private static final User USER_TEST = UserFactory.getUser();

    public static VerificationTokenPassword getVerificationTokenPassword() {
        return VerificationTokenPassword.builder()
                .id(1L)
                .token(UUID.fromString("acbc31b6-19d4-4751-8fce-54dc4babd113"))
                .dataExpiracao(LocalDateTime.now().plusMinutes(15))
                .user(USER_TEST)
                .build();
    }

    public static VerificationTokenPassword getVerificationTokenPasswordExpired() {
        VerificationTokenPassword verification = getVerificationTokenPassword();
        verification.setDataExpiracao(LocalDateTime.now().minusMinutes(1));

        return verification;
    }
}
