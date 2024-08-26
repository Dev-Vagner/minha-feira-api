package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteProductServiceTest {

    @InjectMocks
    private DeleteProductService tested;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    @DisplayName("Deve setar produto como inativo com sucesso")
    void deveInativarProduto() {
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        Long idUser = 2L;

        Mockito.when(searchProductFromUserService.byId(idProduct, idUser)).thenReturn(product);

        tested.delete(idProduct, idUser);

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Product productInactive = productCaptor.getValue();

        Assertions.assertFalse(productInactive.isActive());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID do produto enviado for inválido para aquele usuário")
    void deveRetornarErroQuandoIdProdutoInvalido() {
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        Long idUser = 2L;

        Mockito.doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProduct, idUser);

        Assertions.assertThrows(ProductInvalidException.class, () -> tested.delete(idProduct, idUser));

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
        Mockito.verify(productRepository, Mockito.never()).save(product);
    }
}