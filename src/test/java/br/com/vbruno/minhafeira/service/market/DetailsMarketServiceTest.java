package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.response.market.DetailsMarketResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailsMarketServiceTest {

    @InjectMocks
    private DetailsMarketService tested;

    @Mock
    private SearchMarketFromUserService searchMarketFromUserService;

    @Mock
    private ProductQuantityRepository productQuantityRepository;

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
    @DisplayName("Deve retornar os dados detalhados da feira com sucesso")
    void deveRetornarDadosDetalhadosDaFeira() {
        Market market = MarketFactory.getMarket();
        ProductQuantity productQuantity = MarketFactory.getProductQuantity();
        Long idMarket = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchMarketFromUserService.byId(idMarket, user.getId())).thenReturn(market);
        when(productQuantityRepository.findAllByMarketId(idMarket)).thenReturn(List.of(productQuantity));

        DetailsMarketResponse detailsMarketResponse = tested.details(idMarket);

        verify(searchMarketFromUserService).byId(idMarket, user.getId());
        verify(productQuantityRepository).findAllByMarketId(idMarket);

        assertEquals(market.getId(), detailsMarketResponse.getId());
        assertEquals(market.getDateMarket(), detailsMarketResponse.getDateMarket());
        assertEquals(market.getTotalValue(), detailsMarketResponse.getTotalValue());
        assertEquals(market.getObservation(), detailsMarketResponse.getObservation());
        assertNotNull(detailsMarketResponse.getListProductsQuantities());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID da feira enviado for inválida para aquele usuário")
    void deveRetornarErroQuandoIdFeiraInvalido() {
        Long idMarket = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(MarketInvalidException.class)
                .when(searchMarketFromUserService).byId(idMarket, user.getId());

        assertThrows(MarketInvalidException.class, () -> tested.details(idMarket));

        verify(searchMarketFromUserService).byId(idMarket, user.getId());
        verify(productQuantityRepository, never()).findAllByMarketId(idMarket);
    }
}