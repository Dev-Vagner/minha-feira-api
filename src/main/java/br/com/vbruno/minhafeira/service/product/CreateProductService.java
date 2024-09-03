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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProductService {

    @Autowired
    private ValidateUniqueProductFromUserService validateUniqueProductFromUserService;

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public IdResponse register(CreateProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        validateUniqueProductFromUserService.validate(request.getName(), user.getId());

        Product product = new Product();

        Product productRemoved = searchProductFromUserService.byNameAndNotActive(request.getName(), user.getId());
        if(productRemoved != null) {
            product.setId(productRemoved.getId());
        }

        if(request.getCategoryId() != null) {
            Category category = searchCategoryFromUserService.byId(request.getCategoryId(), user.getId());
            product.setCategory(category);
        }

        product.setName(request.getName());
        product.setUser(user);
        product.setActive(true);

        productRepository.save(product);

        return IdResponseMapper.toResponse(product.getId());
    }
}
