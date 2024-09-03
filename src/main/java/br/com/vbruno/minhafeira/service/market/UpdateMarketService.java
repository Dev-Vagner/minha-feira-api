package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateProductQuantityRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import br.com.vbruno.minhafeira.service.market.validate.ValidateUniqueProductFromMarketService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UpdateMarketService {

    @Autowired
    private ValidateUniqueProductFromMarketService validateUniqueProductFromMarketService;

    @Autowired
    private SearchProductFromUserService searchProductFromUserService;

    @Autowired
    private SearchMarketFromUserService searchMarketFromUserService;

    @Autowired
    private ProductQuantityRepository productQuantityRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Transactional
    public IdResponse update(Long idMarket, UpdateMarketRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Market market = searchMarketFromUserService.byId(idMarket, user.getId());

        List<Long> listIdsProducts = request.getListProductsQuantities().stream()
                .map(UpdateProductQuantityRequest::getProductId).toList();

        validateUniqueProductFromMarketService.validate(listIdsProducts);

        market.setDateMarket(request.getDateMarket());
        market.setTotalValue(request.getTotalValue());
        market.setObservation(request.getObservation());

        marketRepository.save(market);

        List<ProductQuantity> listProductsQuantitiesValidate = getListValidateProductsQuantities(request.getListProductsQuantities(), user.getId(), market);

        productQuantityRepository.deleteAllByMarketId(market.getId());

        productQuantityRepository.saveAll(listProductsQuantitiesValidate);

        return IdResponseMapper.toResponse(market.getId());
    }

    private List<ProductQuantity> getListValidateProductsQuantities(List<UpdateProductQuantityRequest> list, Long idUser, Market market) {
        return list.stream()
                .map(productQuantity -> {
                    Product product = searchProductFromUserService.byId(productQuantity.getProductId(), idUser);

                    ProductQuantity productQuantityValidate = new ProductQuantity();
                    productQuantityValidate.setProduct(product);
                    productQuantityValidate.setQuantity(productQuantity.getQuantity());
                    productQuantityValidate.setUnitValue(productQuantity.getUnitValue());
                    productQuantityValidate.setMarket(market);

                    return productQuantityValidate;
                })
                .toList();
    }
}
