package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.product.CreateProductRequest;
import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.service.product.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
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

    @GetMapping
    public List<ListProductResponse> list() {
        return listProductService.list();
    }

    @GetMapping("category/{idCategory}")
    public List<ListProductResponse> listByCategory(@PathVariable Long idCategory) {
        return listProductByCategoryService.listByCategory(idCategory);
    }

    @GetMapping("/{idProduct}")
    public DetailsProductResponse details(@PathVariable Long idProduct) {
        return detailsProductService.details(idProduct);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateProductRequest request) {
        return createProductService.register(request);
    }

    @PutMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idProduct, @Valid @RequestBody UpdateProductRequest request) {
        return updateProductService.update(idProduct, request);
    }

    @DeleteMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idProduct) {
        deleteProductService.delete(idProduct);
    }
}
