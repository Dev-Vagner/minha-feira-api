package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListProductByCategoryServiceTest {

    @InjectMocks
    private ListProductByCategoryService tested;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Mock
    private ProductRepository productRepository;

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
    @DisplayName("Deve listar todos os produtos ativos do usuário de uma categoria específica com sucesso")
    void deveListarTodosProdutosDeUmaCategoriaDoUsuario() {
        Product product = ProductFactory.getProductWithCategory();
        Long idCategory = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();
        int expectedListSize = 1;

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(productRepository.findAllByUserIdAndCategoryIdAndActiveTrue(user.getId(), idCategory)).thenReturn(Collections.singletonList(product));

        List<ListProductResponse> listProductByCategoryReturned = tested.listByCategory(idCategory);

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
        verify(productRepository).findAllByUserIdAndCategoryIdAndActiveTrue(user.getId(), idCategory);

        assertEquals(expectedListSize, listProductByCategoryReturned.size());
        assertEquals(product.getId(), listProductByCategoryReturned.get(0).getId());
        assertEquals(product.getName(), listProductByCategoryReturned.get(0).getName());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID da categoria enviada for inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalido() {
        Long idCategory = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, user.getId());

        assertThrows(CategoryInvalidException.class, () -> tested.listByCategory(idCategory));

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
        verify(productRepository, never()).findAllByUserIdAndCategoryIdAndActiveTrue(user.getId(), idCategory);
    }
}