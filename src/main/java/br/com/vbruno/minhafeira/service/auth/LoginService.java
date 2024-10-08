package br.com.vbruno.minhafeira.service.auth;

import br.com.vbruno.minhafeira.DTO.request.auth.LoginRequest;
import br.com.vbruno.minhafeira.DTO.response.auth.TokenAuthResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public TokenAuthResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return new TokenAuthResponse(token);
    }
}
