package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.service.market.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/markets")
public class MarketController {

    @Autowired
    private ListMarketService listMarketService;

    @Autowired
    private DetailsMarketService detailsMarketService;

    @Autowired
    private CreateMarketService createMarketService;

    @Autowired
    private UpdateMarketService updateMarketService;

    @Autowired
    private DeleteMarketService deleteMarketService;

    @GetMapping("/user/{idUser}")
    public Page<ListMarketResponse> list(@PathVariable Long idUser, Pageable pageable) {
        return listMarketService.list(idUser, pageable);
    }

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

    @DeleteMapping("/{idMarket}/user/{idUser}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idMarket, @PathVariable Long idUser) {
        deleteMarketService.delete(idMarket, idUser);
    }
}
