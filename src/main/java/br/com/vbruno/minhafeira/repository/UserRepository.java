package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
