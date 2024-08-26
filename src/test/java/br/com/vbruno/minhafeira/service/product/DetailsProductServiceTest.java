package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DetailsProductServiceTest {

    @InjectMocks
    private DetailsProductService tested;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

    @Test
    @DisplayName("Deve retornar os dados detalhados do produto, que tenha categoria, com sucesso")
    void deveRetornarDadosDetalhadosProdutoComCategoria() {
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        Long idUser = 2L;

        Mockito.when(searchProductFromUserService.byId(idProduct, idUser)).thenReturn(product);

        DetailsProductResponse detailsProductResponse = tested.details(idProduct, idUser);

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);

        Assertions.assertEquals(product.getId(), detailsProductResponse.getId());
        Assertions.assertEquals(product.getName(), detailsProductResponse.getName());
        Assertions.assertEquals(product.getCategory().getName(), detailsProductResponse.getCategory());
    }

    @Test
    @DisplayName("Deve retornar os dados detalhados do produto, que não tenha categoria, com sucesso")
    void deveRetornarDadosDetalhadosProdutoSemCategoria() {
        Product product = ProductFactory.getProductNotCategory();
        Long idProduct = 1L;
        Long idUser = 2L;

        Mockito.when(searchProductFromUserService.byId(idProduct, idUser)).thenReturn(product);

        DetailsProductResponse detailsProductResponse = tested.details(idProduct, idUser);

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);

        Assertions.assertEquals(product.getId(), detailsProductResponse.getId());
        Assertions.assertEquals(product.getName(), detailsProductResponse.getName());
        Assertions.assertNull(detailsProductResponse.getCategory());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID do produto enviado for inválido para aquele usuário")
    void deveRetornarErroQuandoIdProdutoInvalido() {
        Long idProduct = 1L;
        Long idUser = 2L;

        Mockito.doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProduct, idUser);

        Assertions.assertThrows(ProductInvalidException.class, () -> tested.details(idProduct, idUser));

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
    }
}