package br.com.vbruno.minhafeira.service.market;

import br.com.vbruno.minhafeira.DTO.request.market.UpdateMarketRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Market;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.ProductQuantity;
import br.com.vbruno.minhafeira.exception.MarketInvalidException;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.exception.ProductNotUniqueMarketException;
import br.com.vbruno.minhafeira.factory.MarketFactory;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.repository.MarketRepository;
import br.com.vbruno.minhafeira.repository.ProductQuantityRepository;
import br.com.vbruno.minhafeira.service.market.search.SearchMarketFromUserService;
import br.com.vbruno.minhafeira.service.market.validate.ValidateUniqueProductFromMarketService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    @Test
    @DisplayName("Deve editar os dados da feira com sucesso")
    void deveEditarFeira() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Market market = MarketFactory.getMarket();
        Product product = ProductFactory.getProductWithCategory();
        Long idMarket = 1L;
        Long idUser = 1L;

        Mockito.when(searchMarketFromUserService.byId(idMarket, idUser)).thenReturn(market);
        Mockito.when(searchProductFromUserService.byId(idProductFromList, idUser)).thenReturn(product);

        IdResponse idResponse = tested.update(idMarket, idUser, updateMarketRequest);

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        Mockito.verify(marketRepository).save(marketCaptor.capture());
        Mockito.verify(searchProductFromUserService).byId(idProductFromList, idUser);
        Mockito.verify(productQuantityRepository).deleteAllByMarketId(market.getId());
        Mockito.verify(productQuantityRepository).saveAll(listProductQuantityCaptor.capture());

        Market marketSaved = marketCaptor.getValue();
        List<ProductQuantity> listProductQuantitySaved = listProductQuantityCaptor.getValue();

        Assertions.assertEquals(marketSaved.getId(), idResponse.getId());
        Assertions.assertEquals(marketSaved, listProductQuantitySaved.get(0).getMarket());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar feira mas o ID da feira for inválido para aquele usuário")
    void deveRetornarErroQuandoIdFeiraInvalidoParaUsuario() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Long idMarket = 1L;
        Long idUser = 1L;

        Mockito.doThrow(MarketInvalidException.class)
                .when(searchMarketFromUserService).byId(idMarket, idUser);

        Assertions.assertThrows(MarketInvalidException.class, () -> tested.update(idMarket, idUser, updateMarketRequest));

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(validateUniqueProductFromMarketService, Mockito.never()).validate(listIdProduct);
        Mockito.verify(marketRepository, Mockito.never()).save(marketCaptor.capture());
        Mockito.verify(searchProductFromUserService, Mockito.never()).byId(idProductFromList, idUser);
        Mockito.verify(productQuantityRepository, Mockito.never()).deleteAllByMarketId(idMarket);
        Mockito.verify(productQuantityRepository, Mockito.never()).saveAll(listProductQuantityCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando houver produtos repetidos na feira")
    void deveRetornarErroQuandoProdutosRepetidosNaFeira() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Market market = MarketFactory.getMarket();
        Long idMarket = 1L;
        Long idUser = 1L;

        Mockito.when(searchMarketFromUserService.byId(idMarket, idUser)).thenReturn(market);

        Mockito.doThrow(ProductNotUniqueMarketException.class)
                .when(validateUniqueProductFromMarketService).validate(listIdProduct);

        Assertions.assertThrows(ProductNotUniqueMarketException.class, () -> tested.update(idMarket, idUser, updateMarketRequest));

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        Mockito.verify(marketRepository, Mockito.never()).save(marketCaptor.capture());
        Mockito.verify(searchProductFromUserService, Mockito.never()).byId(idProductFromList, idUser);
        Mockito.verify(productQuantityRepository, Mockito.never()).deleteAllByMarketId(idMarket);
        Mockito.verify(productQuantityRepository, Mockito.never()).saveAll(listProductQuantityCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando ID de algum produto da feira for inválido para aquele usuário")
    void deveRetornarErroQuandoAlgumProdutoDaFeiraForInvalidoParaUsuario() {
        UpdateMarketRequest updateMarketRequest = MarketFactory.getUpdateMarketRequest();
        Long idProductFromList = updateMarketRequest.getListProductsQuantities().get(0).getProductId();
        List<Long> listIdProduct = List.of(idProductFromList);
        Market market = MarketFactory.getMarket();
        Long idMarket = 1L;
        Long idUser = 1L;

        Mockito.when(searchMarketFromUserService.byId(idMarket, idUser)).thenReturn(market);

        Mockito.doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProductFromList, idUser);

        Assertions.assertThrows(ProductInvalidException.class, () -> tested.update(idMarket, idUser, updateMarketRequest));

        Mockito.verify(searchMarketFromUserService).byId(idMarket, idUser);
        Mockito.verify(validateUniqueProductFromMarketService).validate(listIdProduct);
        Mockito.verify(marketRepository).save(marketCaptor.capture());
        Mockito.verify(searchProductFromUserService).byId(idProductFromList, idUser);
        Mockito.verify(productQuantityRepository, Mockito.never()).deleteAllByMarketId(idMarket);
        Mockito.verify(productQuantityRepository, Mockito.never()).saveAll(listProductQuantityCaptor.capture());
    }
}