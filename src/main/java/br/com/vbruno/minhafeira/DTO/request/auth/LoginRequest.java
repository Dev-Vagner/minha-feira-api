package br.com.vbruno.minhafeira.DTO.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Preencha o email")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Preencha a senha")
    private String password;
}
