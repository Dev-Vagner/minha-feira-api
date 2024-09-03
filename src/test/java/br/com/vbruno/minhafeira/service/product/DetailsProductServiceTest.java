package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.DetailsProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailsProductServiceTest {

    @InjectMocks
    private DetailsProductService tested;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

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
    @DisplayName("Deve retornar os dados detalhados do produto, que tenha categoria, com sucesso")
    void deveRetornarDadosDetalhadosProdutoComCategoria() {
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProduct, user.getId())).thenReturn(product);

        DetailsProductResponse detailsProductResponse = tested.details(idProduct);

        verify(searchProductFromUserService).byId(idProduct, user.getId());

        assertEquals(product.getId(), detailsProductResponse.getId());
        assertEquals(product.getName(), detailsProductResponse.getName());
        assertEquals(product.getCategory().getName(), detailsProductResponse.getCategory());
    }

    @Test
    @DisplayName("Deve retornar os dados detalhados do produto, que não tenha categoria, com sucesso")
    void deveRetornarDadosDetalhadosProdutoSemCategoria() {
        Product product = ProductFactory.getProductNotCategory();
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProduct, user.getId())).thenReturn(product);

        DetailsProductResponse detailsProductResponse = tested.details(idProduct);

        verify(searchProductFromUserService).byId(idProduct, user.getId());

        assertEquals(product.getId(), detailsProductResponse.getId());
        assertEquals(product.getName(), detailsProductResponse.getName());
        assertNull(detailsProductResponse.getCategory());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID do produto enviado for inválido para aquele usuário")
    void deveRetornarErroQuandoIdProdutoInvalido() {
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProduct, user.getId());

        assertThrows(ProductInvalidException.class, () -> tested.details(idProduct));

        verify(searchProductFromUserService).byId(idProduct, user.getId());
    }
}