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

import java.time.LocalDate;

@RestController
@RequestMapping("/markets")
public class MarketController {

    @Autowired
    private ListMarketService listMarketService;

    @Autowired
    private ListMarketByRangeDateService listMarketByRangeDateService;

    @Autowired
    private DetailsMarketService detailsMarketService;

    @Autowired
    private CreateMarketService createMarketService;

    @Autowired
    private UpdateMarketService updateMarketService;

    @Autowired
    private DeleteMarketService deleteMarketService;

    @GetMapping
    public Page<ListMarketResponse> list(Pageable pageable) {
        return listMarketService.list(pageable);
    }

    @GetMapping("/byRangeDate")
    public Page<ListMarketResponse> listByRangeDate(@RequestParam(name = "startDate") LocalDate startDate,
                                                    @RequestParam(name = "endDate") LocalDate endDate,
                                                    Pageable pageable) {
        return listMarketByRangeDateService.listByRangeDate(startDate, endDate, pageable);
    }

    @GetMapping("/{idMarket}")
    public DetailsMarketResponse details(@PathVariable Long idMarket) {
        return detailsMarketService.details(idMarket);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateMarketRequest request) {
        return createMarketService.register(request);
    }

    @PutMapping("/{idMarket}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idMarket, @Valid @RequestBody UpdateMarketRequest request) {
        return updateMarketService.update(idMarket, request);
    }

    @DeleteMapping("/{idMarket}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idMarket) {
        deleteMarketService.delete(idMarket);
    }
}
