package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.mapper.category.DetailsCategoryMapper;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailsCategoryService {

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;


    public DetailsCategoryResponse details(Long idCategory, Long idUser) {
        Category category = searchCategoryFromUserService.byId(idCategory, idUser);

        return DetailsCategoryMapper.toResponse(category);
    }
}
