package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.user.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.request.user.UpdateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.user.DetailsUserResponse;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.service.user.CreateUserService;
import br.com.vbruno.minhafeira.service.user.DeleteUserService;
import br.com.vbruno.minhafeira.service.user.DetailsUserService;
import br.com.vbruno.minhafeira.service.user.UpdateUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private DetailsUserService detailsUserService;

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private UpdateUserService updateUserService;

    @Autowired
    private DeleteUserService deleteUserService;

    @GetMapping("/{id}")
    public DetailsUserResponse details(@PathVariable Long id) {
        return detailsUserService.details(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateUserRequest request) {
        return createUserService.register(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@Valid @RequestBody UpdateUserRequest request) {
        return updateUserService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteUserService.delete(id);
    }
}
