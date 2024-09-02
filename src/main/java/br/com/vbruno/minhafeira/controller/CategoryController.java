package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.category.CreateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.service.category.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
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

    @GetMapping
    public List<ListCategoryResponse> list() {
        return listCategoryService.list();
    }

    @GetMapping("/{idCategory}")
    public DetailsCategoryResponse details(@PathVariable Long idCategory) {
        return detailsCategoryService.details(idCategory);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateCategoryRequest request) {
        return createCategoryService.register(request);
    }

    @PutMapping("/{idCategory}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idCategory, @Valid @RequestBody UpdateCategoryRequest request) {
        return updateCategoryService.update(idCategory, request);
    }

    @DeleteMapping("/{idCategory}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idCategory) {
        deleteCategoryService.delete(idCategory);
    }
}
