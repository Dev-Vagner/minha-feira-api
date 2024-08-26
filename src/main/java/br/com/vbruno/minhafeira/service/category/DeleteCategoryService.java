package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteCategoryService {

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void delete(Long idCategory, Long idUser) {
        searchCategoryFromUserService.byId(idCategory, idUser);

        categoryRepository.deleteById(idCategory);
    }
}
