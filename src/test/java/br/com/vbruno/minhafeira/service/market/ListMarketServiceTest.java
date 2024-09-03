package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.ListMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import org.junit.jupiter.api.*;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMarketServiceTest {

    @InjectMocks
    private ListMarketService tested;

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
    @DisplayName("Deve listar todas as feiras do usuário, de forma paginada, com sucesso")
    void deveListarTodasFeirasDoUsuario() {
        Market market = MarketFactory.getMarket();
        List<Market> listMarkets = List.of(market);
        int expectedListSize = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Market> pagedResponse = new PageImpl<>(listMarkets);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(marketRepository.findAllByUserId(user.getId(), pageable))
                .thenReturn(pagedResponse);

        Page<ListMarketResponse> listMarketsReturned = tested.list(pageable);

        verify(marketRepository).findAllByUserId(user.getId(), pageable);

        assertEquals(expectedListSize, listMarketsReturned.getSize());
        assertEquals(market.getId(), listMarketsReturned.getContent().get(0).getId());
        assertEquals(market.getDateMarket(), listMarketsReturned.getContent().get(0).getDateMarket());
        assertEquals(market.getTotalValue(), listMarketsReturned.getContent().get(0).getTotalValue());
    }

}