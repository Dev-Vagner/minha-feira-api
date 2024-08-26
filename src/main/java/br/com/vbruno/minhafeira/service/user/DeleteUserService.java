package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteUserService {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void delete(Long id) {
        searchUserService.byId(id);

        userRepository.deleteById(id);
    }
}
