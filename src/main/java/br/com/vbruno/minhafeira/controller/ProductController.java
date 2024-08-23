package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.category.CreateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.request.product.CreateProductRequest;
import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.service.product.CreateProductService;
import br.com.vbruno.minhafeira.service.product.DeleteProductService;
import br.com.vbruno.minhafeira.service.product.DetailsProductService;
import br.com.vbruno.minhafeira.service.product.UpdateProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private DetailsProductService detailsProductService;

    @Autowired
    private CreateProductService createProductService;

    @Autowired
    private UpdateProductService updateProductService;

    @Autowired
    private DeleteProductService deleteProductService;

    @GetMapping("/{idProduct}/user/{idUser}")
    public DetailsProductResponse details(@PathVariable Long idProduct, @PathVariable Long idUser) {
        return detailsProductService.details(idProduct, idUser);
    }

    @PostMapping("/user/{idUser}")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@PathVariable Long idUser, @Valid @RequestBody CreateProductRequest request) {
        return createProductService.register(idUser, request);
    }

    @PutMapping("/{idProduct}/user/{idUser}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idProduct, @PathVariable Long idUser, @Valid @RequestBody UpdateProductRequest request) {
        return updateProductService.update(idProduct, idUser, request);
    }

    @DeleteMapping("/{idProduct}/user/{idUser}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idProduct, @PathVariable Long idUser) {
        deleteProductService.delete(idProduct, idUser);
    }
}
