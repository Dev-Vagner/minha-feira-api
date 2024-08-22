package br.com.vbruno.minhafeira.DTO.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {

    @NotBlank(message = "O nome da categoria não pode estar em branco")
    @Size(max = 50, message = "O nome da categoria deve ter no máximo 50 caracteres")
    private String name;
}
