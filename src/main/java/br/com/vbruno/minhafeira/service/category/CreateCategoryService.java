package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.request.category.CreateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.validate.ValidateUniqueCategoryFromUserService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCategoryService {

    @Autowired
    private ValidateUniqueCategoryFromUserService validateUniqueCategoryFromUserService;

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public IdResponse register(Long idUser, CreateCategoryRequest request) {
        User user = searchUserService.byId(idUser);

        validateUniqueCategoryFromUserService.validate(request.getName(), idUser);

        Category category = new Category();
        category.setName(request.getName());
        category.setUser(user);

        categoryRepository.save(category);

        return IdResponseMapper.toResponse(category.getId());
    }
}
