package br.com.vbruno.minhafeira.DTO.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRecoveryPasswordRequest {

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "O email deve ter um formato válido")
    private String email;
}
