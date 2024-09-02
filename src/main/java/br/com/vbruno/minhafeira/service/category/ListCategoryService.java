package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.category.ListCategoryMapper;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCategoryService {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ListCategoryResponse> list() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return categoryRepository.findAllByUserId(user.getId()).stream()
                .map(ListCategoryMapper::toResponse)
                .toList();
    }
}
