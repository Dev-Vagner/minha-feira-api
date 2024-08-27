package br.com.vbruno.minhafeira.factory;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.CreateProductQuantityRequest;

import java.util.List;

public class MarketFactory {

    public static CreateMarketRequest getCreateMarketRequest() {
        CreateProductQuantityRequest productQuantity = new CreateProductQuantityRequest(1L, 5);
        return new CreateMarketRequest(List.of(productQuantity));
    }
}
