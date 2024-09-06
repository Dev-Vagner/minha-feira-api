package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.category.CreateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.infra.security.SecurityConfiguration;
import br.com.vbruno.minhafeira.service.category.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categoria dos produtos", description = "Endpoints relacionados a entidade Categoria")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class CategoryController {

    @Autowired
    private ListCategoryService listCategoryService;

    @Autowired
    private DetailsCategoryService detailsCategoryService;

    @Autowired
    private CreateCategoryService createCategoryService;

    @Autowired
    private UpdateCategoryService updateCategoryService;

    @Autowired
    private DeleteCategoryService deleteCategoryService;

    @Operation(summary = "Lista todas as categorias do usuário logado", description = "Lista todas as categorias de produtos que o usuário logado cadastrou no sistema")
    @ApiResponse(responseCode = "200", description = "Listagem das categorias feita com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping
    public List<ListCategoryResponse> list() {
        return listCategoryService.list();
    }

    @Operation(summary = "Mostra os dados de uma categoria do usuário logado", description = "Mostra os dados de uma categoria do usuário logado através do ID da categoria")
    @ApiResponse(responseCode = "200", description = "Dados da categoria retornado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "ID da categoria enviado não corresponde a nenhuma categoria do usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping("/{idCategory}")
    public DetailsCategoryResponse details(@PathVariable Long idCategory) {
        return detailsCategoryService.details(idCategory);
    }

    @Operation(summary = "Cadastra uma nova categoria para o usuário logado", description = "Cadastra uma nova categoria para o usuário logado")
    @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "O usuário logado já cadastrou uma categoria com o mesmo nome")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateCategoryRequest request) {
        return createCategoryService.register(request);
    }

    @Operation(summary = "Altera o nome de uma categoria do usuário logado", description = "Altera o nome de uma categoria do usuário logado")
    @ApiResponse(responseCode = "200", description = "Categoria alterada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "O usuário logado já cadastrou uma categoria com o mesmo nome ou o ID da categoria enviado é inválido para o usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PutMapping("/{idCategory}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idCategory, @Valid @RequestBody UpdateCategoryRequest request) {
        return updateCategoryService.update(idCategory, request);
    }

    @Operation(summary = "Deleta uma categoria do usuário logado", description = "Deleta uma categoria do usuário logado")
    @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "ID da categoria enviado não corresponde a nenhuma categoria do usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @DeleteMapping("/{idCategory}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idCategory) {
        deleteCategoryService.delete(idCategory);
    }
}
