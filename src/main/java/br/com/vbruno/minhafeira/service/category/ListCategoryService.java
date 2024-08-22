package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.mapper.category.ListCategoryMapper;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCategoryService {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ListCategoryResponse> list(Long idUser) {
        searchUserService.byId(idUser);

        return categoryRepository.findAllByUserId(idUser).stream()
                .map(ListCategoryMapper::toResponse)
                .toList();
    }
}
