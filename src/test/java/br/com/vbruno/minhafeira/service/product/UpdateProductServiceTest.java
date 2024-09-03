package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.exception.ProductRegisteredException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import br.com.vbruno.minhafeira.service.product.validate.ValidateUniqueProductFromUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @InjectMocks
    private UpdateProductService tested;

    @Mock
    private ValidateUniqueProductFromUserService validateUniqueProductFromUserService;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

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
    @DisplayName("Deve editar os dados do produto com sucesso quando for passado categoria")
    void deveEditarDadosProdutoComCategoria() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestWithCategory();
        Product product = ProductFactory.getProductWithCategory();
        Category category = CategoryFactory.getCategory();
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProduct, user.getId())).thenReturn(product);
        when(searchCategoryFromUserService.byId(updateProductRequest.getCategoryId(), user.getId())).thenReturn(category);

        IdResponse idResponse = tested.update(idProduct, updateProductRequest);

        verify(searchProductFromUserService).byId(idProduct, user.getId());
        verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService).byId(updateProductRequest.getCategoryId(), user.getId());
        verify(productRepository).save(productCaptor.capture());

        Product productUpdate = productCaptor.getValue();

        assertEquals(productUpdate.getId(), idResponse.getId());
        assertEquals(category, productUpdate.getCategory());
        assertEquals(updateProductRequest.getName(), productUpdate.getName());
    }

    @Test
    @DisplayName("Deve editar os dados do produto com sucesso quando não for passado categoria")
    void deveEditarDadosProdutoSemCategoria() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestNotCategory();
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProduct, user.getId())).thenReturn(product);

        IdResponse idResponse = tested.update(idProduct, updateProductRequest);

        verify(searchProductFromUserService).byId(idProduct, user.getId());
        verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService, never()).byId(updateProductRequest.getCategoryId(), user.getId());
        verify(productRepository).save(productCaptor.capture());

        Product productSaved = productCaptor.getValue();

        assertEquals(productSaved.getId(), idResponse.getId());
        assertNull(productSaved.getCategory());
        assertEquals(updateProductRequest.getName(), productSaved.getName());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar produto mas o ID do produto for inválido para aquele usuário")
    void deveRetornarErroQuandoIdProdutoInvalidoParaUsuario() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestNotCategory();
        Long idProduct = 1L;
        Long idCategory = 2L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProduct, user.getId());

        assertThrows(ProductInvalidException.class, () -> tested.update(idProduct, updateProductRequest));

        verify(searchProductFromUserService).byId(idProduct, user.getId());
        verify(validateUniqueProductFromUserService, never()).validate(updateProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService, never()).byId(idCategory, user.getId());
        verify(productRepository, never()).save(productCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar alterar o nome do produto e já existir um produto cadastrado pelo usuário com o mesmo nome e ativo no sistema")
    void deveRetornarErroQuandoNomeProdutoJaCadastradoPeloUsuario() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestNotCategory();
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        Long idCategory = 2L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProduct, user.getId())).thenReturn(product);

        doThrow(ProductRegisteredException.class)
                .when(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), user.getId());

        assertThrows(ProductRegisteredException.class, () -> tested.update(idProduct, updateProductRequest));

        verify(searchProductFromUserService).byId(idProduct, user.getId());
        verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService, never()).byId(idCategory, user.getId());
        verify(productRepository, never()).save(productCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar produto com um ID de categoria inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalidoParaUsuario() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestWithCategory();
        Product product = ProductFactory.getProductNotCategory();
        Long idProduct = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byId(idProduct, user.getId())).thenReturn(product);

        doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(updateProductRequest.getCategoryId(), user.getId());

        assertThrows(CategoryInvalidException.class, () -> tested.update(idProduct, updateProductRequest));

        verify(searchProductFromUserService).byId(idProduct, user.getId());
        verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService).byId(updateProductRequest.getCategoryId(), user.getId());
        verify(productRepository, never()).save(productCaptor.capture());
    }
}






