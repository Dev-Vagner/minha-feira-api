package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ListProductServiceTest {

    @InjectMocks
    private ListProductService tested;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Deve listar todos os produtos ativos do usuário com sucesso")
    void deveListarTodosProdutosDoUsuario() {
        Product product = ProductFactory.getProductWithCategory();
        Long idUser = 1L;
        int expectedListSize = 1;

        Mockito.when(productRepository.findAllByUserIdAndActiveTrue(idUser)).thenReturn(Collections.singletonList(product));

        List<ListProductResponse> listProductReturned = tested.list(idUser);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(productRepository).findAllByUserIdAndActiveTrue(idUser);

        Assertions.assertEquals(expectedListSize, listProductReturned.size());
        Assertions.assertEquals(product.getId(), listProductReturned.get(0).getId());
        Assertions.assertEquals(product.getName(), listProductReturned.get(0).getName());
    }

}