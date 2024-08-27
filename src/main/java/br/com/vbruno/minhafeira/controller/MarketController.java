package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.service.market.CreateMarketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/markets")
public class MarketController {

    @Autowired
    private CreateMarketService createMarketService;

    @PostMapping("/user/{idUser}")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@PathVariable Long idUser, @Valid @RequestBody CreateMarketRequest request) {
        return createMarketService.register(idUser, request);
    }
}
