package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.category.DetailsCategoryMapper;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DetailsCategoryService {

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;


    public DetailsCategoryResponse details(Long idCategory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Category category = searchCategoryFromUserService.byId(idCategory, user.getId());

        return DetailsCategoryMapper.toResponse(category);
    }
}
