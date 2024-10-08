package br.com.vbruno.minhafeira.repository;

import br.com.vbruno.minhafeira.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    UserDetails findByEmail(String email);
}
