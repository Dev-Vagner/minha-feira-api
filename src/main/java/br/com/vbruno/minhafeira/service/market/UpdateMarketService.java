package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateProductQuantityRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.mapper.IdResponseMapper;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import br.com.vbruno.minhafeira.service.market.validate.ValidateUniqueProductFromMarketService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public IdResponse update(Long idMarket, Long idUser, UpdateMarketRequest request) {
        Market market = searchMarketFromUserService.byId(idMarket, idUser);

        List<Long> listIdsProducts = request.getListProductsQuantities().stream()
                .map(UpdateProductQuantityRequest::getProductId).toList();

        validateUniqueProductFromMarketService.validate(listIdsProducts);

        market.setDateMarket(request.getDateMarket());
        market.setTotalValue(request.getTotalValue());
        market.setObservation(request.getObservation());

        marketRepository.save(market);

        List<ProductQuantity> listProductQuantity = request.getListProductsQuantities().stream()
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

        productQuantityRepository.deleteAllByMarketId(market.getId());

        productQuantityRepository.saveAll(listProductQuantity);

        return IdResponseMapper.toResponse(market.getId());
    }
}
