package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.request.market.CreateMarketRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.exception.ProductMarketNotUniqueException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.validate.ValidateUniqueProductFromMarketService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CreateMarketServiceTest {

    @InjectMocks
    private CreateMarketService tested;

    @Mock
    private ValidateUniqueProductFromMarketService validateUniqueProductFromMarketService;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

    @Mock
    private ProductQuantityRepository productQuantityRepository;

    @Mock
    private MarketRepository marketRepository;

    @Captor
    private ArgumentCaptor<ProductQuantity> productQuantityCaptor;

    @Captor
    private ArgumentCaptor<Market> marketCaptor;

    @Test
    @DisplayName("Deve cadastrar uma nova feira com sucesso")
    void deveCadastrarNovaFeira() {
        CreateMarketRequest createMarketRequest = MarketFactory.getCreateMarketRequest();
        Long idProductFromList = createMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Product product = ProductFactory.getProductWithCategory();
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);
        Mockito.when(searchProductFromUserService.byId(idProductFromList, idUser)).thenReturn(product);

        IdResponse idResponse = tested.register(idUser, createMarketRequest);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        Mockito.verify(searchProductFromUserService).byId(idProductFromList, idUser);
        Mockito.verify(marketRepository).save(marketCaptor.capture());
        Mockito.verify(productQuantityRepository).save(productQuantityCaptor.capture());

        Market marketSaved = marketCaptor.getValue();
        ProductQuantity productQuantitySaved = productQuantityCaptor.getValue();

        Assertions.assertEquals(marketSaved.getId(), idResponse.getId());
        Assertions.assertEquals(marketSaved, productQuantitySaved.getMarket());
    }

    @Test
    @DisplayName("Deve retornar erro quando houver produtos repetidos na feira")
    void deveRetornarErroQuandoProdutosRepetidosNaFeira() {
        CreateMarketRequest createMarketRequest = MarketFactory.getCreateMarketRequest();
        Long idProductFromList = createMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);

        Mockito.doThrow(ProductMarketNotUniqueException.class)
                .when(validateUniqueProductFromMarketService).validate(listIdProduct);

        Assertions.assertThrows(ProductMarketNotUniqueException.class, () -> tested.register(idUser, createMarketRequest));

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        Mockito.verify(searchProductFromUserService, Mockito.never()).byId(idProductFromList, idUser);
        Mockito.verify(marketRepository, Mockito.never()).save(marketCaptor.capture());
        Mockito.verify(productQuantityRepository, Mockito.never()).save(productQuantityCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando ID de algum produto da feira for inválido para aquele usuário")
    void deveRetornarErroQuandoAlgumProdutoDaFeiraForInvalidoParaUsuario() {
        CreateMarketRequest createMarketRequest = MarketFactory.getCreateMarketRequest();
        Long idProductFromList = createMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);

        Mockito.doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProductFromList, idUser);

        Assertions.assertThrows(ProductInvalidException.class, () -> tested.register(idUser, createMarketRequest));

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        Mockito.verify(searchProductFromUserService).byId(idProductFromList, idUser);
        Mockito.verify(marketRepository, Mockito.never()).save(marketCaptor.capture());
        Mockito.verify(productQuantityRepository, Mockito.never()).save(productQuantityCaptor.capture());
    }
}