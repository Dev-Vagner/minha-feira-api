package br.com.vbruno.minhafeira.DTO.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

    @NotBlank(message = "O nome do produto não pode estar em branco")
    @Size(max = 50, message = "O nome do produto deve ter no máximo 50 caracteres")
    private String name;

    private Long categoryId;
}
