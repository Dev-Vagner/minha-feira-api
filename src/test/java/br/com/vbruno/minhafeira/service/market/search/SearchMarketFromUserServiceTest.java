package br.com.vbruno.minhafeira.service.market.search;

import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SearchMarketFromUserServiceTest {

    @InjectMocks
    private SearchMarketFromUserService tested;

    @Mock
    private MarketRepository marketRepository;

    @Test
    @DisplayName("Deve retornar feira quando o ID da feira for válido para o usuário passado")
    void deveRetornarFeiraQuandoIdValidoParaUsuario() {
        Market market = MarketFactory.getMarket();
        Long idUser = 1L;

        Mockito.when(marketRepository.findByIdAndUserId(market.getId(), idUser)).thenReturn(Optional.of(market));

        Market marketReturned = tested.byId(market.getId(), idUser);

        Mockito.verify(marketRepository).findByIdAndUserId(market.getId(), idUser);

        Assertions.assertEquals(market, marketReturned);
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID da feira for inválido para o usuário passado")
    void deveRetornarErroQuandoIdFeiraInvalidoParaUsuario() {
        Long idMarket = 1L;
        Long idUser = 2L;

        MarketInvalidException exception =
                Assertions.assertThrows(MarketInvalidException.class, () -> tested.byId(idMarket, idUser));

        Mockito.verify(marketRepository).findByIdAndUserId(idMarket, idUser);

        Assertions.assertEquals("Feira inválida", exception.getMessage());
    }

}