package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.exception.ProductNotUniqueMarketException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import br.com.vbruno.minhafeira.service.market.validate.ValidateUniqueProductFromMarketService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMarketServiceTest {

    @InjectMocks
    private UpdateMarketService tested;

    @Mock
    private ValidateUniqueProductFromMarketService validateUniqueProductFromMarketService;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

    @Mock
    private SearchMarketFromUserService searchMarketFromUserService;

    @Mock
    private ProductQuantityRepository productQuantityRepository;

    @Mock
    private MarketRepository marketRepository;

    @Captor
    private ArgumentCaptor<List<ProductQuantity>> listProductQuantityCaptor;

    @Captor
    private ArgumentCaptor<Market> marketCaptor;

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
    @DisplayName("Deve editar os dados da feira com sucesso")
    void deveEditarFeira() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Market market = MarketFactory.getMarket();
        Product product = ProductFactory.getProductWithCategory();
        Long idMarket = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchMarketFromUserService.byId(idMarket, user.getId())).thenReturn(market);
        when(searchProductFromUserService.byId(idProductFromList, user.getId())).thenReturn(product);

        IdResponse idResponse = tested.update(idMarket, updateMarketRequest);

        verify(searchMarketFromUserService).byId(idMarket, user.getId());
        verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        verify(marketRepository).save(marketCaptor.capture());
        verify(searchProductFromUserService).byId(idProductFromList, user.getId());
        verify(productQuantityRepository).deleteAllByMarketId(market.getId());
        verify(productQuantityRepository).saveAll(listProductQuantityCaptor.capture());

        Market marketSaved = marketCaptor.getValue();
        List<ProductQuantity> listProductQuantitySaved = listProductQuantityCaptor.getValue();

        assertEquals(marketSaved.getId(), idResponse.getId());
        assertEquals(marketSaved, listProductQuantitySaved.get(0).getMarket());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar feira mas o ID da feira for inválido para aquele usuário")
    void deveRetornarErroQuandoIdFeiraInvalidoParaUsuario() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Long idMarket = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(MarketInvalidException.class)
                .when(searchMarketFromUserService).byId(idMarket, user.getId());

        assertThrows(MarketInvalidException.class, () -> tested.update(idMarket, updateMarketRequest));

        verify(searchMarketFromUserService).byId(idMarket, user.getId());
        verify(validateUniqueProductFromMarketService, never()).validate(listIdProduct);
        verify(marketRepository, never()).save(marketCaptor.capture());
        verify(searchProductFromUserService, never()).byId(idProductFromList, user.getId());
        verify(productQuantityRepository, never()).deleteAllByMarketId(idMarket);
        verify(productQuantityRepository, never()).saveAll(listProductQuantityCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando houver produtos repetidos na feira")
    void deveRetornarErroQuandoProdutosRepetidosNaFeira() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Market market = MarketFactory.getMarket();
        Long idMarket = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchMarketFromUserService.byId(idMarket, user.getId())).thenReturn(market);

        doThrow(ProductNotUniqueMarketException.class)
                .when(validateUniqueProductFromMarketService).validate(listIdProduct);

        assertThrows(ProductNotUniqueMarketException.class, () -> tested.update(idMarket, updateMarketRequest));

        verify(searchMarketFromUserService).byId(idMarket, user.getId());
        verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        verify(marketRepository, never()).save(marketCaptor.capture());
        verify(searchProductFromUserService, never()).byId(idProductFromList, user.getId());
        verify(productQuantityRepository, never()).deleteAllByMarketId(idMarket);
        verify(productQuantityRepository, never()).saveAll(listProductQuantityCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando ID de algum produto da feira for inválido para aquele usuário")
    void deveRetornarErroQuandoAlgumProdutoDaFeiraForInvalidoParaUsuario() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Market market = MarketFactory.getMarket();
        Long idMarket = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchMarketFromUserService.byId(idMarket, user.getId())).thenReturn(market);

        doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProductFromList, user.getId());

        assertThrows(ProductInvalidException.class, () -> tested.update(idMarket, updateMarketRequest));

        verify(searchMarketFromUserService).byId(idMarket, user.getId());
        verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        verify(marketRepository).save(marketCaptor.capture());
        verify(searchProductFromUserService).byId(idProductFromList, user.getId());
        verify(productQuantityRepository, never()).deleteAllByMarketId(idMarket);
        verify(productQuantityRepository, never()).saveAll(listProductQuantityCaptor.capture());
    }
}