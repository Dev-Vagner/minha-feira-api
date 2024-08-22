package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.category.CreateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.service.category.CreateCategoryService;
import br.com.vbruno.minhafeira.service.category.DeleteCategoryService;
import br.com.vbruno.minhafeira.service.category.DetailsCategoryService;
import br.com.vbruno.minhafeira.service.category.UpdateCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private DetailsCategoryService detailsCategoryService;

    @Autowired
    private CreateCategoryService createCategoryService;

    @Autowired
    private UpdateCategoryService updateCategoryService;

    @Autowired
    private DeleteCategoryService deleteCategoryService;

    @GetMapping("/{idCategory}/user/{idUser}")
    public DetailsCategoryResponse details(@PathVariable Long idCategory, @PathVariable Long idUser) {
        return detailsCategoryService.details(idCategory, idUser);
    }

    @PostMapping("/user/{idUser}")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@PathVariable Long idUser, @Valid @RequestBody CreateCategoryRequest request) {
        return createCategoryService.register(idUser, request);
    }

    @PutMapping("/{idCategory}/user/{idUser}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idCategory, @PathVariable Long idUser, @Valid @RequestBody UpdateCategoryRequest request) {
        return updateCategoryService.update(idCategory, idUser, request);
    }

    @DeleteMapping("/{idCategory}/user/{idUser}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idCategory, @PathVariable Long idUser) {
        deleteCategoryService.delete(idCategory, idUser);
    }
}
