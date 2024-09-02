package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteCategoryService {

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public void delete(Long idCategory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        searchCategoryFromUserService.byId(idCategory, user.getId());

        categoryRepository.deleteById(idCategory);
    }
}
