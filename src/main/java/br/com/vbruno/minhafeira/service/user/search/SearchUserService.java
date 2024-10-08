package br.com.vbruno.minhafeira.service.user.search;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchUserService {

    @Autowired
    private UserRepository userRepository;

    public User byId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotRegisteredException("Usuário não encontrado"));
    }

    public User byEmail(String email) {
        User user = (User) userRepository.findByEmail(email);

        if(user == null) throw new UserNotRegisteredException("Email não cadastrado");

        return user;
    }

}
