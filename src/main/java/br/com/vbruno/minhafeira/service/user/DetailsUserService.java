package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.response.user.DetailsUserResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.user.DetailsUserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DetailsUserService {

    public DetailsUserResponse details() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return DetailsUserMapper.toResponse(user);
    }
}
