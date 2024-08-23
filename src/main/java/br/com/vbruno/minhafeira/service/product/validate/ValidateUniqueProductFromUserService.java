package br.com.vbruno.minhafeira.service.product.validate;

import br.com.vbruno.minhafeira.exception.ProductRegisteredException;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateUniqueProductFromUserService {

    @Autowired
    private ProductRepository productRepository;

    public void validate(String nameProduct, Long idUser) {
        boolean existsProduct = productRepository.existsByNameIgnoreCaseAndUserId(nameProduct, idUser);

        if(existsProduct) throw new ProductRegisteredException("Este produto já está cadastrado");
    }
}
