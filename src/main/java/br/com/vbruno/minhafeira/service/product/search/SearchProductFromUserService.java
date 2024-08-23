package br.com.vbruno.minhafeira.service.product.search;

import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchProductFromUserService {

    @Autowired
    private ProductRepository productRepository;

    public Product byId(Long productId, Long userId) {
        return productRepository.findByIdAndUserId(productId, userId)
                .orElseThrow(() -> new ProductInvalidException("Produto inválido"));
    }
}
