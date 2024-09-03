package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.DTO.response.market.DetailsProductQuantityResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.market.DetailsMarketMapper;
import br.com.vbruno.minhafeira.mapper.market.DetailsProductQuantityMapper;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetailsMarketService {

    @Autowired
    private SearchMarketFromUserService searchMarketFromUserService;

    @Autowired
    private ProductQuantityRepository productQuantityRepository;

    public DetailsMarketResponse details(Long idMarket) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Market market = searchMarketFromUserService.byId(idMarket, user.getId());
        List<ProductQuantity> listProductsQuantities = productQuantityRepository.findAllByMarketId(idMarket);

        List<DetailsProductQuantityResponse> listDetailsProductQuantityResponse = getListDetailsProductQuantityResponse(listProductsQuantities);

        DetailsMarketResponse detailsMarketResponse = DetailsMarketMapper.toResponse(market);
        detailsMarketResponse.setListProductsQuantities(listDetailsProductQuantityResponse);

        return detailsMarketResponse;
    }

    private List<DetailsProductQuantityResponse> getListDetailsProductQuantityResponse(List<ProductQuantity> list) {
        return list.stream().map(productQuantity -> {
            DetailsProductQuantityResponse productQuantityResponse = DetailsProductQuantityMapper.toResponse(productQuantity);

            if(productQuantityResponse.getUnitValue() != null) {
                BigDecimal unitValueProduct = productQuantityResponse.getUnitValue();
                BigDecimal quantityProduct = BigDecimal.valueOf(productQuantityResponse.getQuantity());

                BigDecimal productQuantityValue = unitValueProduct.multiply(quantityProduct);
                productQuantityResponse.setTotalValue(productQuantityValue);
            }

            return productQuantityResponse;
        }).toList();
    }
}
