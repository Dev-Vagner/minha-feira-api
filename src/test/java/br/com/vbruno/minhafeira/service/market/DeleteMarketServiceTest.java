package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteMarketServiceTest {

    @InjectMocks
    private DeleteMarketService tested;

    @Mock
    private SearchMarketFromUserService searchMarketFromUserService;

    @Mock
    private MarketRepository marketRepository;

    @Test
    @DisplayName("Deve deletar a feira com sucesso")
    void deveDeletarFeira() {
        Long idMarket = 1L;
        Long idUser = 2L;

        tested.delete(idMarket, idUser);

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(marketRepository).deleteById(idMarket);
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar deletar a feira mas o ID da feira for inválido para aquele usuário")
    void deveRetornarErroQuandoIdFeiraInvalido() {
        Long idMarket = 1L;
        Long idUser = 2L;

        Mockito.doThrow(MarketInvalidException.class)
                .when(searchMarketFromUserService).byId(idMarket, idUser);

        Assertions.assertThrows(MarketInvalidException.class, () -> tested.delete(idMarket, idUser));

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(marketRepository, Mockito.never()).deleteById(idMarket);
    }
}