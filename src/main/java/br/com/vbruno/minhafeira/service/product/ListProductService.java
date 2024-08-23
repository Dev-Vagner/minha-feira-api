package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.mapper.category.ListCategoryMapper;
import br.com.vbruno.minhafeira.mapper.product.ListProductMapper;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListProductService {

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private ProductRepository productRepository;

    public List<ListProductResponse> list(Long idUser) {
        searchUserService.byId(idUser);

        return productRepository.findAllByUserIdAndActiveTrue(idUser).stream()
                .map(ListProductMapper::toResponse)
                .toList();
    }
}
