package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.mapper.user.CreateUserMapper;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.validate.ValidateUniqueEmailUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return IdResponseMapper.toResponse(user.getId());
    }
}
