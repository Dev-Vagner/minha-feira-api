package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteCategoryService {

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    public void delete(Long idCategory, Long idUser) {
        searchCategoryFromUserService.byId(idCategory, idUser);

        categoryRepository.deleteById(idCategory);
    }
}
