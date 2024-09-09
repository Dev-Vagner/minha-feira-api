package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.user.*;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.MessageResponse;
import br.com.vbruno.minhafeira.DTO.response.user.DetailsUserResponse;
import br.com.vbruno.minhafeira.infra.security.SecurityConfiguration;
import br.com.vbruno.minhafeira.service.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuário")
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

    @SecurityRequirement(name = SecurityConfiguration.SECURITY)
    @Operation(summary = "Mostra os dados do usuário logado", description = "Mostra os dados do usuário logado")
    @ApiResponse(responseCode = "200", description = "Dados do usuário retornado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping("/details")
    public DetailsUserResponse details() {
        return detailsUserService.details();
    }

    @Operation(summary = "Cadastra um novo usuário", description = "Cadastra um novo usuário no sistema")
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "422", description = "Email já cadastrado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateUserRequest request) {
        return createUserService.register(request);
    }

    @SecurityRequirement(name = SecurityConfiguration.SECURITY)
    @Operation(summary = "Edita os dados do usuário logado", description = "Edita os dados do usuário logado")
    @ApiResponse(responseCode = "200", description = "Dados do usuário editado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "Email já cadastrado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@Valid @RequestBody UpdateUserRequest request) {
        return updateUserService.update(request);
    }

    @SecurityRequirement(name = SecurityConfiguration.SECURITY)
    @Operation(summary = "Edita a senha do usuário logado", description = "Edita a senha do usuário logado")
    @ApiResponse(responseCode = "200", description = "Senha do usuário editada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "Senha atual enviada (currentPassword) não corresponde a senha cadastrada no banco de dados")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse updatePassword(@Valid @RequestBody UpdateUserPasswordRequest request) {
        return updateUserPasswordService.updatePassword(request);
    }

    @Operation(summary = "Envia email com token  de recuperação de senha", description = "Envia email com token de recuperação de senha, para que o usuário possa setar uma senha nova")
    @ApiResponse(responseCode = "200", description = "Email enviado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "404", description = "Email não encontrado no sistema")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PostMapping("/email-recovery-password")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse emailRecoveryPassword(@Valid @RequestBody EmailRecoveryPasswordRequest request) {
        return recoveryUserPasswordService.sendEmail(request);
    }

    @Operation(summary = "Altera a senha através do token enviado por email", description = "Altera a senha do usuário através do token enviado via email")
    @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "422", description = "O token enviado é inválido")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PutMapping("/recovery-password")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse recoveryPassword(@Valid @RequestBody RecoveryPasswordRequest request) {
        return recoveryUserPasswordService.recoveryPassword(request);
    }

    @SecurityRequirement(name = SecurityConfiguration.SECURITY)
    @Operation(summary = "Deleta o usuário logado", description = "Deleta os dados do usuário logado")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        deleteUserService.delete();
    }
}
