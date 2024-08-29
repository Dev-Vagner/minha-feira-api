package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.exception.RangeDateInvalidException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.market.validate.ValidateRangeDateFromMarketService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ListMarketByRangeDateServiceTest {

    @InjectMocks
    private ListMarketByRangeDateService tested;

    @Mock
    private ValidateRangeDateFromMarketService validateRangeDateFromMarketService;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private MarketRepository marketRepository;

    @Test
    @DisplayName("Deve listar todas as feiras do usuário que estejam entre as datas passadas, de forma paginada, com sucesso")
    void deveListarTodasFeirasDoUsuarioEntreAsDatasPassadas() {
        Long idUser = 1L;
        Market market = MarketFactory.getMarket();
        List<Market> listMarkets = List.of(market);
        int expectedListSize = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Market> pagedResponse = new PageImpl<>(listMarkets);
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 8, 20);

        Mockito.when(marketRepository.findAllByUserIdAndDateMarketBetween(idUser, startDate, endDate, pageable))
                .thenReturn(pagedResponse);

        Page<ListMarketResponse> listMarketsReturned = tested.listByRangeDate(idUser, startDate, endDate, pageable);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateRangeDateFromMarketService).validate(startDate, endDate);
        Mockito.verify(marketRepository).findAllByUserIdAndDateMarketBetween(idUser, startDate, endDate, pageable);

        Assertions.assertEquals(expectedListSize, listMarketsReturned.getSize());
        Assertions.assertEquals(market.getId(), listMarketsReturned.getContent().get(0).getId());
        Assertions.assertEquals(market.getDateMarket(), listMarketsReturned.getContent().get(0).getDateMarket());
        Assertions.assertEquals(market.getTotalValue(), listMarketsReturned.getContent().get(0).getTotalValue());
    }

    @Test
    @DisplayName("Deve retornar erro quando o intervalo de datas for inválido")
    void deveRetornarErroQuandoIntervaloDatasInvalido() {
        Long idUser = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 8, 20);

        Mockito.doThrow(RangeDateInvalidException.class)
                .when(validateRangeDateFromMarketService).validate(startDate, endDate);

        Assertions.assertThrows(RangeDateInvalidException.class, () -> tested.listByRangeDate(idUser, startDate, endDate, pageable));

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateRangeDateFromMarketService).validate(startDate, endDate);
        Mockito.verify(marketRepository, Mockito.never()).findAllByUserIdAndDateMarketBetween(idUser, startDate, endDate, pageable);
    }
}