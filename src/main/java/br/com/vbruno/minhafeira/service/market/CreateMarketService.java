package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.CreateProductQuantityRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateProductQuantityRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.validate.ValidateUniqueProductFromMarketService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CreateMarketService {

    @Autowired
    private ValidateUniqueProductFromMarketService validateUniqueProductFromMarketService;

    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    @Autowired
    private ProductQuantityRepository productQuantityRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Transactional
    public IdResponse register(Long idUser, CreateMarketRequest request) {
        User user = searchUserService.byId(idUser);

        List<Long> listIdsProducts = request.getListProductsQuantities().stream()
                .map(CreateProductQuantityRequest::getProductId).toList();

        validateUniqueProductFromMarketService.validate(listIdsProducts);

        Market market = new Market();
        market.setDateMarket(LocalDate.now());
        market.setObservation(request.getObservation());
        market.setUser(user);

        marketRepository.save(market);

        List<ProductQuantity> listProductQuantityValidate = getListValidateProductsQuantities(request.getListProductsQuantities(), idUser, market);

        productQuantityRepository.saveAll(listProductQuantityValidate);

        return IdResponseMapper.toResponse(market.getId());
    }

    private List<ProductQuantity> getListValidateProductsQuantities(List<CreateProductQuantityRequest> list, Long idUser, Market market) {
        return list.stream()
                .map(productQuantity -> {
                    Product product = searchProductFromUserService.byId(productQuantity.getProductId(), idUser);

                    ProductQuantity productQuantityValidate = new ProductQuantity();
                    productQuantityValidate.setProduct(product);
                    productQuantityValidate.setQuantity(productQuantity.getQuantity());
                    productQuantityValidate.setMarket(market);

                    return productQuantityValidate;
                })
                .toList();
    }
}
