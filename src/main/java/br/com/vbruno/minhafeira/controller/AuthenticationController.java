package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.auth.LoginRequest;
import br.com.vbruno.minhafeira.DTO.response.auth.TokenAuthResponse;
import br.com.vbruno.minhafeira.service.auth.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public TokenAuthResponse login(@RequestBody @Valid LoginRequest request) {
        return loginService.login(request);
    }
}
