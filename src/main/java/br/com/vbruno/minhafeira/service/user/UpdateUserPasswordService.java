package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.UpdateUserPasswordRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.PasswordInvalidException;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateUserPasswordService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public IdResponse updatePassword(UpdateUserPasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();


        boolean isValid = bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword());
        if(!isValid) throw new PasswordInvalidException("A senha enviada não é igual a senha cadastrada");

        String encryptedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return IdResponseMapper.toResponse(user.getId());
    }
}
