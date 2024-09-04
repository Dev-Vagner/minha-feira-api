package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.user.CreateUserRequest;
import br.com.vbruno.minhafeira.DTO.request.user.EmailRecoveryPasswordRequest;
import br.com.vbruno.minhafeira.DTO.request.user.UpdateUserPasswordRequest;
import br.com.vbruno.minhafeira.DTO.request.user.UpdateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.MessageResponse;
import br.com.vbruno.minhafeira.DTO.response.user.DetailsUserResponse;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.service.user.*;
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
    private UpdateUserPasswordService updateUserPasswordService;

    @Autowired
    private RecoveryUserPasswordService recoveryUserPasswordService;

    @Autowired
    private DeleteUserService deleteUserService;

    @GetMapping("/details")
    public DetailsUserResponse details() {
        return detailsUserService.details();
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

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse updatePassword(@Valid @RequestBody UpdateUserPasswordRequest request) {
        return updateUserPasswordService.updatePassword(request);
    }

    @PostMapping("/email-recovery-password")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse emailRecoveryPassword(@Valid @RequestBody EmailRecoveryPasswordRequest request) {
        return recoveryUserPasswordService.sendEmail(request);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        deleteUserService.delete();
    }
}
