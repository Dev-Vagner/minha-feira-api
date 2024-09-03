package br.com.vbruno.minhafeira.DTO.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserPasswordRequest {

    @NotBlank(message = "A senha atual não pode estar em branco")
    @Size(min = 5, message = "A senha atual deve ter pelo menos 5 caracteres")
    private String currentPassword;

    @NotBlank(message = "A nova senha não pode estar em branco")
    @Size(min = 5, message = "A nova senha deve ter pelo menos 5 caracteres")
    private String newPassword;
}
