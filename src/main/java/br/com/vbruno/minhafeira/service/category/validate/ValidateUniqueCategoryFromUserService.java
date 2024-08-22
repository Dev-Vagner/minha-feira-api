package br.com.vbruno.minhafeira.service.category.validate;

import br.com.vbruno.minhafeira.exception.CategoryRegisteredException;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateUniqueCategoryFromUserService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void validate(String nameCategory, Long idUser) {
        boolean existsCategory = categoryRepository.existsByNameIgnoreCaseAndUserId(nameCategory, idUser);

        if(existsCategory) throw new CategoryRegisteredException("Esta categoria já está cadastrada");
    }
}
