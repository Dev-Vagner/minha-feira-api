package br.com.vbruno.minhafeira.factory;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.CreateProductQuantityRequest;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MarketFactory {

    private static final User USER_TEST = UserFactory.getUser();
    private static final Product PRODUCT_TEST = ProductFactory.getProductWithCategory();

    public static Market getMarket() {
        return new Market(1L, LocalDate.now(), new BigDecimal("30"), "Observação teste", USER_TEST);
    }

    public static CreateMarketRequest getCreateMarketRequest() {
        CreateProductQuantityRequest productQuantity = new CreateProductQuantityRequest(1L, 5);

        List<CreateProductQuantityRequest> listProductQuantity = List.of(productQuantity);
        String observation = "Observação teste";

        return new CreateMarketRequest(listProductQuantity, observation);
    }
}
