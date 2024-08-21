package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.mapper.user.CreateUserMapper;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.validate.ValidateUniqueEmailUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    @Autowired
    private ValidateUniqueEmailUserService validateUniqueEmailUserService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public IdResponse register(CreateUserRequest request) {
        validateUniqueEmailUserService.validate(request.getEmail());

        User user = CreateUserMapper.toEntity(request);
        userRepository.save(user);

        return IdResponseMapper.toResponse(user.getId());
    }
}
