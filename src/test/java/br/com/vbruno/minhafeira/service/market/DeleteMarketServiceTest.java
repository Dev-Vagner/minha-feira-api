package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMarketServiceTest {

    @InjectMocks
    private DeleteMarketService tested;

    @Mock
    private SearchMarketFromUserService searchMarketFromUserService;

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
    @DisplayName("Deve deletar a feira com sucesso")
    void deveDeletarFeira() {
        Long idMarket = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        tested.delete(idMarket);

        Mockito.verify(searchMarketFromUserService).byId(idMarket, user.getId());
        Mockito.verify(marketRepository).deleteById(idMarket);
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar deletar a feira mas o ID da feira for inválido para aquele usuário")
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

        assertThrows(MarketInvalidException.class, () -> tested.delete(idMarket));

        verify(searchMarketFromUserService).byId(idMarket, user.getId());
        verify(marketRepository, never()).deleteById(idMarket);
    }
}