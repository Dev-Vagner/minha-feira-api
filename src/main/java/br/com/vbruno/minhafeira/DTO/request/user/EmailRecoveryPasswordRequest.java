package br.com.vbruno.minhafeira.DTO.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRecoveryPasswordRequest {

    @NotBlank(message = "O email do usuário não pode estar em branco")
    @Email(message = "O email do usuário deve ter um formato válido")
    private String email;
}
