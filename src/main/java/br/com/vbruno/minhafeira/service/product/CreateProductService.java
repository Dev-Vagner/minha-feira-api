package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.request.product.CreateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import br.com.vbruno.minhafeira.service.product.validate.ValidateUniqueProductFromUserService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateProductService {

    @Autowired
    private ValidateUniqueProductFromUserService validateUniqueProductFromUserService;

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public IdResponse register(Long idUser, CreateProductRequest request) {
        User user = searchUserService.byId(idUser);

        validateUniqueProductFromUserService.validate(request.getName(), idUser);

        Product product = new Product();

        Product productRemoved = searchProductFromUserService.byNameAndNotActive(request.getName(), idUser);
        if(productRemoved != null) {
            product.setId(productRemoved.getId());
        }

        if(request.getCategoryId() != null) {
            Category category = searchCategoryFromUserService.byId(request.getCategoryId(), idUser);
            product.setCategory(category);
        }

        product.setName(request.getName());
        product.setUser(user);
        product.setActive(true);

        productRepository.save(product);

        return IdResponseMapper.toResponse(product.getId());
    }
}
