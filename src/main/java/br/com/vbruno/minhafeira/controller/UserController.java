package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.request.UpdateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.service.user.CreateUserService;
import br.com.vbruno.minhafeira.service.user.UpdateUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private UpdateUserService updateUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateUserRequest request) {
        return createUserService.register(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return updateUserService.update(id, request);
    }
}
