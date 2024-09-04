package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.VerificationTokenPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenPasswordRepository extends JpaRepository<VerificationTokenPassword, Long> {

    void deleteByUserId(Long userId);

    Optional<VerificationTokenPassword> findByToken(UUID token);
}
