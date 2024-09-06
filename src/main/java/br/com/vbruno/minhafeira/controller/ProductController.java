package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.product.CreateProductRequest;
import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.infra.security.SecurityConfiguration;
import br.com.vbruno.minhafeira.service.product.*;
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
@RequestMapping("/products")
@Tag(name = "Produto", description = "Endpoints relacionados a entidade Produto")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class ProductController {

    @Autowired
    private ListProductService listProductService;

    @Autowired
    private ListProductByCategoryService listProductByCategoryService;

    @Autowired
    private DetailsProductService detailsProductService;

    @Autowired
    private CreateProductService createProductService;

    @Autowired
    private UpdateProductService updateProductService;

    @Autowired
    private DeleteProductService deleteProductService;

    @Operation(summary = "Lista todos os produtos do usuário logado", description = "Lista todos os produtos que o usuário logado cadastrou no sistema")
    @ApiResponse(responseCode = "200", description = "Listagem dos produtos feita com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping
    public List<ListProductResponse> list() {
        return listProductService.list();
    }

    @Operation(summary = "Lista todos os produtos de uma categoria do usuário logado", description = "Lista todos os produtos correspondentes a uma categoria do usuário logado")
    @ApiResponse(responseCode = "200", description = "Listagem dos produtos feita com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "ID da categoria enviado não corresponde a nenhuma categoria do usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping("category/{idCategory}")
    public List<ListProductResponse> listByCategory(@PathVariable Long idCategory) {
        return listProductByCategoryService.listByCategory(idCategory);
    }

    @Operation(summary = "Mostra os dados de um produto do usuário logado", description = "Mostra os dados de um produto do usuário logado através do ID do produto")
    @ApiResponse(responseCode = "200", description = "Dados do produto retornado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "ID do produto enviado não corresponde a nenhum produto do usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping("/{idProduct}")
    public DetailsProductResponse details(@PathVariable Long idProduct) {
        return detailsProductService.details(idProduct);
    }

    @Operation(summary = "Cadastra um novo produto para o usuário logado", description = "Cadastra um novo produto para o usuário logado")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "O usuário logado já cadastrou um produto com o mesmo nome ou o ID da categoria enviado é inválido para o usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateProductRequest request) {
        return createProductService.register(request);
    }

    @Operation(summary = "Altera os dados de um produto do usuário logado", description = "Altera os dados de um produto do usuário logado")
    @ApiResponse(responseCode = "200", description = "Produto alterado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "O usuário logado já cadastrou um produto com o mesmo nome ou o ID da categoria ou o ID do produto enviado é inválido para o usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PutMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idProduct, @Valid @RequestBody UpdateProductRequest request) {
        return updateProductService.update(idProduct, request);
    }

    @Operation(summary = "Deleta um produto do usuário logado", description = "Deleta um produto do usuário logado")
    @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "ID do produto enviado não corresponde a nenhum produto do usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @DeleteMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idProduct) {
        deleteProductService.delete(idProduct);
    }
}
