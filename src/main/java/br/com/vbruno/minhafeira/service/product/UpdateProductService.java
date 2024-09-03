package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
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

import java.util.Objects;

@Service
public class UpdateProductService {

    @Autowired
    private ValidateUniqueProductFromUserService validateUniqueProductFromUserService;

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    @Autowired
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public IdResponse update(Long idProduct, UpdateProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Product product = searchProductFromUserService.byId(idProduct, user.getId());
        
        if(!Objects.equals(request.getName().toUpperCase(), product.getName().toUpperCase())) {
            validateUniqueProductFromUserService.validate(request.getName(), user.getId());
        }

        if(request.getCategoryId() != null) {
            Category category = searchCategoryFromUserService.byId(request.getCategoryId(), user.getId());
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        product.setName(request.getName());

        productRepository.save(product);

        return IdResponseMapper.toResponse(product.getId());
    }
}
