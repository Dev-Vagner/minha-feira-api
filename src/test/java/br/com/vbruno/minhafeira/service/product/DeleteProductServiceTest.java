package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    @DisplayName("Deve setar produto como inativo com sucesso")
    void deveInativarProduto() {
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProduct, user.getId())).thenReturn(product);

        tested.delete(idProduct);

        verify(searchProductFromUserService).byId(idProduct, user.getId());
        verify(productRepository).save(productCaptor.capture());

        Product productInactive = productCaptor.getValue();

        assertFalse(productInactive.isActive());
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID do produto enviado for inválido para aquele usuário")
    void deveRetornarErroQuandoIdProdutoInvalido() {
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProduct, user.getId());

        assertThrows(ProductInvalidException.class, () -> tested.delete(idProduct));

        verify(searchProductFromUserService).byId(idProduct, user.getId());
        verify(productRepository, never()).save(product);
    }
}