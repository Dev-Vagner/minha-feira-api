package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteUserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        userRepository.deleteById(user.getId());
    }
}
