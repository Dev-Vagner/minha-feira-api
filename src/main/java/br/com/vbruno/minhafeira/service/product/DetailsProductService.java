package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.mapper.product.DetailsProductMapper;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetailsProductService {

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    public DetailsProductResponse details(Long idProduct, Long idUser) {
        Product product = searchProductFromUserService.byId(idProduct, idUser);

        if(product.getCategory() == null) {
            return DetailsProductMapper.toResponseBase(product);
        } else {
            return DetailsProductMapper.toResponseCategoryNotNull(product);
        }
    }
}
