package br.com.vbruno.minhafeira.factory;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.CreateProductQuantityRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateProductQuantityRequest;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MarketFactory {

    private static final User USER_TEST = UserFactory.getUser();
    private static final Product PRODUCT_TEST = ProductFactory.getProductWithCategory();

    public static Market getMarket() {
        return Market.builder()
                .id(1L)
                .dateMarket(LocalDate.now())
                .totalValue(new BigDecimal("25.5"))
                .observation("Observação teste")
                .user(USER_TEST)
                .build();
    }

    public static ProductQuantity getProductQuantity() {
        return ProductQuantity.builder()
                .id(1L)
                .product(PRODUCT_TEST)
                .quantity(5)
                .unitValue(new BigDecimal("10.5"))
                .market(getMarket())
                .build();
    }

    public static CreateMarketRequest getCreateMarketRequest() {
        CreateProductQuantityRequest productQuantity = new CreateProductQuantityRequest();
        productQuantity.setProductId(1L);
        productQuantity.setQuantity(5);

        CreateMarketRequest market = new CreateMarketRequest();
        market.setListProductsQuantities(List.of(productQuantity));
        market.setObservation("Observação criação teste");

        return market;
    }

    public static UpdateMarketRequest getUpdateMarketRequest() {
        UpdateProductQuantityRequest productQuantity = new UpdateProductQuantityRequest();
        productQuantity.setProductId(1L);
        productQuantity.setQuantity(5);
        productQuantity.setUnitValue(new BigDecimal("7.25"));

        UpdateMarketRequest market = new UpdateMarketRequest();
        market.setDateMarket(LocalDate.now());
        market.setListProductsQuantities(List.of(productQuantity));
        market.setTotalValue(new BigDecimal("50"));
        market.setObservation("Observação edição teste");

        return market;
    }
}
