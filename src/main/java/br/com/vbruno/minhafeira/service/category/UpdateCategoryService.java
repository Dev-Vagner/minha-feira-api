package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.category.validate.ValidateUniqueCategoryFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public IdResponse update(Long idCategory, UpdateCategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Category category = searchCategoryFromUserService.byId(idCategory, user.getId());

        if(!Objects.equals(request.getName().toUpperCase(), category.getName().toUpperCase())) {
            validateUniqueCategoryFromUserService.validate(request.getName(), user.getId());
        }

        category.setName(request.getName());

        categoryRepository.save(category);

        return IdResponseMapper.toResponse(category.getId());
    }
}
