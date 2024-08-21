package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserService {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private UserRepository userRepository;

    public void delete(Long id) {
        User user = searchUserService.byId(id);

        userRepository.deleteById(user.getId());
    }
}
