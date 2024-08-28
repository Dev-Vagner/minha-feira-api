package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DetailsMarketServiceTest {

    @InjectMocks
    private DetailsMarketService tested;

    @Mock
    private SearchMarketFromUserService searchMarketFromUserService;

    @Mock
    private ProductQuantityRepository productQuantityRepository;

    @Test
    @DisplayName("Deve retornar os dados detalhados da feira com sucesso")
    void deveRetornarDadosDetalhadosDaFeira() {
        Market market = MarketFactory.getMarket();
        ProductQuantity productQuantity = MarketFactory.getProductQuantity();
        Long idMarket = 1L;
        Long idUser = 2L;

        Mockito.when(searchMarketFromUserService.byId(idMarket, idUser)).thenReturn(market);
        Mockito.when(productQuantityRepository.findAllByMarketId(idMarket)).thenReturn(List.of(productQuantity));

        DetailsMarketResponse detailsMarketResponse = tested.details(idMarket, idUser);

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(productQuantityRepository).findAllByMarketId(idMarket);

        Assertions.assertEquals(market.getId(), detailsMarketResponse.getId());
        Assertions.assertEquals(market.getDateMarket(), detailsMarketResponse.getDateMarket());
        Assertions.assertEquals(market.getTotalValue(), detailsMarketResponse.getTotalValue());
        Assertions.assertEquals(market.getObservation(), detailsMarketResponse.getObservation());
        Assertions.assertNotNull(detailsMarketResponse.getListProductsQuantities());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID da feira enviado for inválida para aquele usuário")
    void deveRetornarErroQuandoIdFeiraInvalido() {
        Long idMarket = 1L;
        Long idUser = 2L;

        Mockito.doThrow(MarketInvalidException.class)
                .when(searchMarketFromUserService).byId(idMarket, idUser);

        Assertions.assertThrows(MarketInvalidException.class, () -> tested.details(idMarket, idUser));

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(productQuantityRepository, Mockito.never()).findAllByMarketId(idMarket);
    }
}