package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.service.user.CreateUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private CreateUserService createUserService;

    @PostMapping
    public IdResponse register(@Valid @RequestBody CreateUserRequest request) {
        return createUserService.register(request);
    }
}
