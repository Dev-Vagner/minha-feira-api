package br.com.vbruno.minhafeira.DTO.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryPasswordRequest {

    @NotBlank(message = "O token não pode estar em branco")
    private String token;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres")
    private String newPassword;
}
