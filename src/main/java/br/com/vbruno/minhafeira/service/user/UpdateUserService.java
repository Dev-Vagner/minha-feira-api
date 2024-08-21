package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.UpdateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UpdateUserService {

    @Autowired
    private ValidateUniqueEmailUserService validateUniqueEmailUserService;

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private UserRepository userRepository;

    public IdResponse update(Long id, UpdateUserRequest request) {
        User user = searchUserService.byId(id);

        if(!Objects.equals(request.getEmail(), user.getEmail())) {
            validateUniqueEmailUserService.validate(request.getEmail());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.save(user);

        return IdResponseMapper.toResponse(user.getId());
    }
}
