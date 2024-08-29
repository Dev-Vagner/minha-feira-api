package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ListMarketServiceTest {

    @InjectMocks
    private ListMarketService tested;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private MarketRepository marketRepository;

    @Test
    @DisplayName("Deve listar todas as feiras do usuário, de forma paginada, com sucesso")
    void deveListarTodasFeirasDoUsuario() {
        Long idUser = 1L;
        Market market = MarketFactory.getMarket();
        List<Market> listMarkets = List.of(market);
        int expectedListSize = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Market> pagedResponse = new PageImpl<>(listMarkets);

        Mockito.when(marketRepository.findAllByUserId(idUser, pageable))
                .thenReturn(pagedResponse);

        Page<ListMarketResponse> listMarketsReturned = tested.list(idUser, pageable);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(marketRepository).findAllByUserId(idUser, pageable);

        Assertions.assertEquals(expectedListSize, listMarketsReturned.getSize());
        Assertions.assertEquals(market.getId(), listMarketsReturned.getContent().get(0).getId());
        Assertions.assertEquals(market.getDateMarket(), listMarketsReturned.getContent().get(0).getDateMarket());
        Assertions.assertEquals(market.getTotalValue(), listMarketsReturned.getContent().get(0).getTotalValue());
    }

}