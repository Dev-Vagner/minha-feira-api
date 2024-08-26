package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
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
class ListProductByCategoryServiceTest {

    @InjectMocks
    private ListProductByCategoryService tested;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Deve listar todos os produtos ativos do usuário de uma categoria específica com sucesso")
    void deveListarTodosProdutosDeUmaCategoriaDoUsuario() {
        Product product = ProductFactory.getProductWithCategory();
        Long idCategory = 1L;
        Long idUser = 2L;
        int expectedListSize = 1;

        Mockito.when(productRepository.findAllByUserIdAndCategoryIdAndActiveTrue(idUser, idCategory)).thenReturn(Collections.singletonList(product));

        List<ListProductResponse> listProductByCategoryReturned = tested.listByCategory(idCategory, idUser);

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
        Mockito.verify(productRepository).findAllByUserIdAndCategoryIdAndActiveTrue(idUser, idCategory);

        Assertions.assertEquals(expectedListSize, listProductByCategoryReturned.size());
        Assertions.assertEquals(product.getId(), listProductByCategoryReturned.get(0).getId());
        Assertions.assertEquals(product.getName(), listProductByCategoryReturned.get(0).getName());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID da categoria enviada for inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalido() {
        Long idCategory = 1L;
        Long idUser = 2L;

        Mockito.doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, idUser);

        Assertions.assertThrows(CategoryInvalidException.class, () -> tested.listByCategory(idCategory, idUser));

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
        Mockito.verify(productRepository, Mockito.never()).findAllByUserIdAndCategoryIdAndActiveTrue(idUser, idCategory);
    }
}