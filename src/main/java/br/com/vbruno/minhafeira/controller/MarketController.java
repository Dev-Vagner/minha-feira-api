package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.infra.security.SecurityConfiguration;
import br.com.vbruno.minhafeira.service.market.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/markets")
@Tag(name = "Feira", description = "Endpoints relacionados a entidade Feira")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
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

    @Operation(summary = "Lista todas as feiras do usuário logado", description = "Lista todas as feiras, de forma paginada, que o usuário logado cadastrou no sistema")
    @ApiResponse(responseCode = "200", description = "Listagem das feiras feita com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping
    public Page<ListMarketResponse> list(Pageable pageable) {
        return listMarketService.list(pageable);
    }

    @Operation(summary = "Lista todas as feiras, em um intervalo de datas, do usuário logado", description = "Lista todas as feiras, em um intervalo de datas e de forma paginada, que o usuário logado cadastrou no sistema,")
    @ApiResponse(responseCode = "200", description = "Listagem das feiras feita com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping("/byRangeDate")
    public Page<ListMarketResponse> listByRangeDate(@RequestParam(name = "startDate") LocalDate startDate,
                                                    @RequestParam(name = "endDate") LocalDate endDate,
                                                    Pageable pageable) {
        return listMarketByRangeDateService.listByRangeDate(startDate, endDate, pageable);
    }

    @Operation(summary = "Mostra os dados de uma feira do usuário logado", description = "Mostra os dados de uma feira do usuário logado através do ID da feira")
    @ApiResponse(responseCode = "200", description = "Dados da feira retornado com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "ID da feira enviado não corresponde a nenhuma feira do usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @GetMapping("/{idMarket}")
    public DetailsMarketResponse details(@PathVariable Long idMarket) {
        return detailsMarketService.details(idMarket);
    }

    @Operation(summary = "Cadastra uma nova feira para o usuário logado", description = "Cadastra uma nova feira para o usuário logado")
    @ApiResponse(responseCode = "201", description = "Feira cadastrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "Há ID's de produtos repetidos na feira ou o ID de algum produto enviado é inválido para o usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(@Valid @RequestBody CreateMarketRequest request) {
        return createMarketService.register(request);
    }

    @Operation(summary = "Altera os dados de uma feira do usuário logado", description = "Altera os dados de uma feira do usuário logado")
    @ApiResponse(responseCode = "200", description = "Feira alterada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados enviados em um formato inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "Há ID's de produtos repetidos na feira ou o ID da feira ou o ID de algum produto enviado é inválido para o usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @PutMapping("/{idMarket}")
    @ResponseStatus(HttpStatus.OK)
    public IdResponse update(@PathVariable Long idMarket, @Valid @RequestBody UpdateMarketRequest request) {
        return updateMarketService.update(idMarket, request);
    }

    @Operation(summary = "Deleta uma feira do usuário logado", description = "Deleta uma feira do usuário logado")
    @ApiResponse(responseCode = "204", description = "Feira deletada com sucesso")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "422", description = "ID da feira enviado não corresponde a nenhuma feira do usuário logado")
    @ApiResponse(responseCode = "500", description = "Problemas internos no servidor")
    @DeleteMapping("/{idMarket}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long idMarket) {
        deleteMarketService.delete(idMarket);
    }
}
