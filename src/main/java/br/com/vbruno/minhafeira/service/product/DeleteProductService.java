package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteProductService {

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void delete(Long idProduct, Long idUser) {
        Product product = searchProductFromUserService.byId(idProduct, idUser);

        product.setActive(false);

        productRepository.save(product);
    }
}
