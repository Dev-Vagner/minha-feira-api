package br.com.vbruno.minhafeira.service.user.validate;

import br.com.vbruno.minhafeira.exception.EmailRegisteredException;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateUniqueEmailUserService {

    @Autowired
    private UserRepository userRepository;

    public void validate(String email) {
        boolean existsEmail = userRepository.existsByEmail(email);

        if(existsEmail) throw new EmailRegisteredException("Este email já está cadastrado!");
    }
}
