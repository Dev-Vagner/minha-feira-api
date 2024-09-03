package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProductService {

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void delete(Long idProduct) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Product product = searchProductFromUserService.byId(idProduct, user.getId());

        product.setActive(false);

        productRepository.save(product);
    }
}
