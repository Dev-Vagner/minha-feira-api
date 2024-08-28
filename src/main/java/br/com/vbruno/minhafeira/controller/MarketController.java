package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.service.market.CreateMarketService;
import br.com.vbruno.minhafeira.service.market.DetailsMarketService;
import br.com.vbruno.minhafeira.service.market.UpdateMarketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/markets")
public class MarketController {

    @Autowired
    private DetailsMarketService detailsMarketService;

    @Autowired
    private CreateMarketService createMarketService;

    @Autowired
    private UpdateMarketService updateMarketService;

    @GetMapping("/{idMarket}/user/{idUser}")
    public DetailsMarketResponse details(@PathVariable Long idMarket, @PathVariable Long idUser) {
        return detailsMarketService.details(idMarket, idUser);
    }

    @PostMapping("/user/{idUser}")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@PathVariable Long idUser, @Valid @RequestBody CreateMarketRequest request) {
        return createMarketService.register(idUser, request);
    }

    @PutMapping("/{idMarket}/user/{idUser}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idMarket, @PathVariable Long idUser, @Valid @RequestBody UpdateMarketRequest request) {
        return updateMarketService.update(idMarket, idUser, request);
    }
}
