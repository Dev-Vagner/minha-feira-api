package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.RangeDateInvalidException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.service.market.validate.ValidateRangeDateFromMarketService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMarketByRangeDateServiceTest {

    @InjectMocks
    private ListMarketByRangeDateService tested;

    @Mock
    private ValidateRangeDateFromMarketService validateRangeDateFromMarketService;

    @Mock
    private MarketRepository marketRepository;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void beforeTests() {
        securityContextHolderMock = Mockito.mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void afterTests() {
        securityContextHolderMock.close();
    }

    @Test
    @DisplayName("Deve listar todas as feiras do usuário que estejam entre as datas passadas, de forma paginada, com sucesso")
    void deveListarTodasFeirasDoUsuarioEntreAsDatasPassadas() {
        Market market = MarketFactory.getMarket();
        List<Market> listMarkets = List.of(market);
        int expectedListSize = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Market> pagedResponse = new PageImpl<>(listMarkets);
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 8, 20);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(marketRepository.findAllByUserIdAndDateMarketBetween(user.getId(), startDate, endDate, pageable))
                .thenReturn(pagedResponse);

        Page<ListMarketResponse> listMarketsReturned = tested.listByRangeDate(startDate, endDate, pageable);

        verify(validateRangeDateFromMarketService).validate(startDate, endDate);
        verify(marketRepository).findAllByUserIdAndDateMarketBetween(user.getId(), startDate, endDate, pageable);

        assertEquals(expectedListSize, listMarketsReturned.getSize());
        assertEquals(market.getId(), listMarketsReturned.getContent().get(0).getId());
        assertEquals(market.getDateMarket(), listMarketsReturned.getContent().get(0).getDateMarket());
        assertEquals(market.getTotalValue(), listMarketsReturned.getContent().get(0).getTotalValue());
    }

    @Test
    @DisplayName("Deve retornar erro quando o intervalo de datas for inválido")
    void deveRetornarErroQuandoIntervaloDatasInvalido() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 8, 20);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(RangeDateInvalidException.class)
                .when(validateRangeDateFromMarketService).validate(startDate, endDate);

        assertThrows(RangeDateInvalidException.class, () -> tested.listByRangeDate(startDate, endDate, pageable));

        verify(validateRangeDateFromMarketService).validate(startDate, endDate);
        verify(marketRepository, never()).findAllByUserIdAndDateMarketBetween(user.getId(), startDate, endDate, pageable);
    }
}