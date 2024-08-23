package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.mapper.product.ListProductMapper;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListProductByCategoryService {

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private ProductRepository productRepository;

    public List<ListProductResponse> listByCategory(Long idCategory, Long idUser) {
        searchCategoryFromUserService.byId(idCategory, idUser);

        return productRepository.findAllByUserIdAndCategoryIdAndActiveTrue(idUser, idCategory).stream()
                .map(ListProductMapper::toResponse)
                .toList();
    }
}
