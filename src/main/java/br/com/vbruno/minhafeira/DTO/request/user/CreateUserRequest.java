package br.com.vbruno.minhafeira.DTO.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "O nome do usuário não pode estar em branco")
    @Size(min = 3, max = 50, message = "O nome do usuário deve ter entre 3 e 50 caracteres")
    private String name;

    @NotBlank(message = "O email do usuário não pode estar em branco")
    @Email(message = "O email do usuário deve ter um formato válido")
    private String email;

    @NotBlank(message = "A senha do usuário não pode estar em branco")
    @Size(min = 3, max = 50, message = "A senha do usuário deve ter entre 3 e 50 caracteres")
    private String password;
}
