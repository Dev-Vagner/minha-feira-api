package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.category.validate.ValidateUniqueCategoryFromUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UpdateCategoryService {

    @Autowired
    private ValidateUniqueCategoryFromUserService validateUniqueCategoryFromUserService;

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public IdResponse update(Long idCategory, Long idUser, UpdateCategoryRequest request) {
        Category category = searchCategoryFromUserService.byId(idCategory, idUser);

        if(!Objects.equals(request.getName(), category.getName())) {
            validateUniqueCategoryFromUserService.validate(request.getName(), idUser);
        }

        category.setName(request.getName());

        categoryRepository.save(category);

        return IdResponseMapper.toResponse(category.getId());
    }
}
