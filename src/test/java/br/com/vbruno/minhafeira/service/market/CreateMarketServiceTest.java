package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.exception.ProductNotUniqueMarketException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.validate.ValidateUniqueProductFromMarketService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class CreateMarketServiceTest {

    @InjectMocks
    private CreateMarketService tested;

    @Mock
    private ValidateUniqueProductFromMarketService validateUniqueProductFromMarketService;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

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
    @DisplayName("Deve cadastrar uma nova feira com sucesso")
    void deveCadastrarNovaFeira() {
        CreateMarketRequest createMarketRequest = MarketFactory.getCreateMarketRequest();
        Long idProductFromList = createMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Product product = ProductFactory.getProductWithCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProductFromList, user.getId())).thenReturn(product);

        IdResponse idResponse = tested.register(createMarketRequest);

        verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        verify(marketRepository).save(marketCaptor.capture());
        verify(searchProductFromUserService).byId(idProductFromList, user.getId());
        verify(productQuantityRepository).saveAll(listProductQuantityCaptor.capture());

        Market marketSaved = marketCaptor.getValue();
        List<ProductQuantity> listProductQuantitySaved = listProductQuantityCaptor.getValue();

        assertEquals(marketSaved.getId(), idResponse.getId());
        assertEquals(marketSaved, listProductQuantitySaved.get(0).getMarket());
    }

    @Test
    @DisplayName("Deve retornar erro quando houver produtos repetidos na feira")
    void deveRetornarErroQuandoProdutosRepetidosNaFeira() {
        CreateMarketRequest createMarketRequest = MarketFactory.getCreateMarketRequest();
        Long idProductFromList = createMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(ProductNotUniqueMarketException.class)
                .when(validateUniqueProductFromMarketService).validate(listIdProduct);

        assertThrows(ProductNotUniqueMarketException.class, () -> tested.register(createMarketRequest));

        verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        verify(marketRepository, never()).save(marketCaptor.capture());
        verify(searchProductFromUserService, never()).byId(idProductFromList, user.getId());
        verify(productQuantityRepository, never()).saveAll(listProductQuantityCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando ID de algum produto da feira for inválido para aquele usuário")
    void deveRetornarErroQuandoAlgumProdutoDaFeiraForInvalidoParaUsuario() {
        CreateMarketRequest createMarketRequest = MarketFactory.getCreateMarketRequest();
        Long idProductFromList = createMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProductFromList, user.getId());

        assertThrows(ProductInvalidException.class, () -> tested.register(createMarketRequest));

        verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        verify(marketRepository).save(marketCaptor.capture());
        verify(searchProductFromUserService).byId(idProductFromList, user.getId());
        verify(productQuantityRepository, never()).saveAll(listProductQuantityCaptor.capture());
    }
}