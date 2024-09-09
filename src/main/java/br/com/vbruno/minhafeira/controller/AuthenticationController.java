package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.auth.LoginRequest;
import br.com.vbruno.minhafeira.DTO.response.auth.TokenAuthResponse;
import br.com.vbruno.minhafeira.service.auth.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação")
public class AuthenticationController {

    @Autowired
    private LoginService loginService;

    @Operation(summary = "Autenticação do usuário", description = "Endpoint para realização da autenticação do usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Autenticação não autorizada")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PostMapping
    public TokenAuthResponse login(@RequestBody @Valid LoginRequest request) {
        return loginService.login(request);
    }
}
