package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.response.DetailsUserResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.user.DetailsUserMapper;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailsUserService {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private UserRepository userRepository;

    public DetailsUserResponse details(Long id) {
        User user = searchUserService.byId(id);

        return DetailsUserMapper.toResponse(user);
    }
}
