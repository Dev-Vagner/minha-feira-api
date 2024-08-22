package br.com.vbruno.minhafeira.service.category.search;

import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchCategoryFromUserService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category byId(Long categoryId, Long userId) {
        return categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new CategoryInvalidException("Categoria inválida"));
    }
}
