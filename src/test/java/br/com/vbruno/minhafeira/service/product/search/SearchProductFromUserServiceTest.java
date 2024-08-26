package br.com.vbruno.minhafeira.service.product.search;

import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SearchProductFromUserServiceTest {

    @InjectMocks
    private SearchProductFromUserService tested;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Deve retornar produto quando o ID do produto for válido para o usuário passado")
    void deveRetornarProdutoQuandoIdValidoParaUsuario() {
        Product product = ProductFactory.getProductWithCategory();
        Long idUser = 1L;

        Mockito.when(productRepository.findByIdAndUserIdAndActiveTrue(product.getId(), idUser)).thenReturn(Optional.of(product));

        Product productReturned = tested.byId(product.getId(), idUser);

        Mockito.verify(productRepository).findByIdAndUserIdAndActiveTrue(product.getId(), idUser);

        Assertions.assertEquals(product, productReturned);
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID do produto for inválido para o usuário passado")
    void deveRetornarErroQuandoIdProdutoInvalidoParaUsuario() {
        Long idProduct = 1L;
        Long idUser = 2L;

        ProductInvalidException exception =
                Assertions.assertThrows(ProductInvalidException.class, () -> tested.byId(idProduct, idUser));

        Mockito.verify(productRepository).findByIdAndUserIdAndActiveTrue(idProduct, idUser);

        Assertions.assertEquals("Produto inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar um produto não ativo quando o nome do produto corresponder a um nome de produto removido pelo usuário")
    void deveRetornarProdutoRemovidoPeloUsuario() {
        Product productRemoved = ProductFactory.getProductNotActive();
        Long idUser = 1L;

        Mockito.when(productRepository.findByNameIgnoreCaseAndUserIdAndActiveFalse(productRemoved.getName(), idUser)).thenReturn(productRemoved);

        Product productReturned = tested.byNameAndNotActive(productRemoved.getName(), idUser);

        Mockito.verify(productRepository).findByNameIgnoreCaseAndUserIdAndActiveFalse(productRemoved.getName(), idUser);

        Assertions.assertEquals(productRemoved, productReturned);
        Assertions.assertFalse(productReturned.isActive());
    }
}