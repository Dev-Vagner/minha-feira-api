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

    @GetMapping("/user/{idUser}")
    public List<ListCategoryResponse> list(@PathVariable Long idUser) {
        return listCategoryService.list(idUser);
    }

    @GetMapping("/{idCategory}/user/{idUser}")
    public DetailsCategoryResponse details(@PathVariable Long idCategory, @PathVariable Long idUser) {
        return detailsCategoryService.details(idCategory, idUser);
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

    @DeleteMapping("/{idCategory}/user/{idUser}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idCategory, @PathVariable Long idUser) {
        deleteCategoryService.delete(idCategory, idUser);
    }
}
