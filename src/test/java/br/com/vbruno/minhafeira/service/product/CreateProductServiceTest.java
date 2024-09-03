package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.request.product.CreateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
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
class CreateProductServiceTest {

    @InjectMocks
    private CreateProductService tested;

    @Mock
    private ValidateUniqueProductFromUserService validateUniqueProductFromUserService;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

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
    @DisplayName("Deve cadastrar um novo produto com categoria com sucesso")
    void deveCadastrarNovoProdutoComCategoria() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        Category category = CategoryFactory.getCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), user.getId())).thenReturn(null);
        when(searchCategoryFromUserService.byId(createProductRequest.getCategoryId(), user.getId())).thenReturn(category);

        IdResponse idResponse = tested.register(createProductRequest);


        verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), user.getId());
        verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService).byId(createProductRequest.getCategoryId(), user.getId());
        verify(productRepository).save(productCaptor.capture());

        Product product = productCaptor.getValue();

        assertEquals(product.getId(), idResponse.getId());
        assertEquals(category, product.getCategory());
        assertEquals(createProductRequest.getName(), product.getName());
        assertEquals(user, product.getUser());
        assertTrue(product.isActive());
    }

    @Test
    @DisplayName("Deve cadastrar um novo produto sem categoria com sucesso")
    void deveCadastrarNovoProdutoSemCategoria() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestNotCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), user.getId())).thenReturn(null);

        IdResponse idResponse = tested.register(createProductRequest);

        verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), user.getId());
        verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService, never()).byId(createProductRequest.getCategoryId(), user.getId());
        verify(productRepository).save(productCaptor.capture());

        Product product = productCaptor.getValue();

        assertEquals(product.getId(), idResponse.getId());
        assertNull(product.getCategory());
        assertEquals(createProductRequest.getName(), product.getName());
        assertEquals(user, product.getUser());
        assertTrue(product.isActive());
    }

    @Test
    @DisplayName("Deve reativar produto, ao invés de criar um novo, quando o nome de um produto que tenha sido removido for igual ao nome do produto que o usuário deseja cadastrar")
    void deveReativarProdutoERetornarIdProdutoIgualIdProdutoRemovido() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        Category category = CategoryFactory.getCategory();
        Long idProductRemoved = 10L;
        Product productRemoved = ProductFactory.getProductNotActive();
        productRemoved.setId(idProductRemoved);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), user.getId())).thenReturn(productRemoved);
        when(searchCategoryFromUserService.byId(createProductRequest.getCategoryId(), user.getId())).thenReturn(category);

        IdResponse idResponse = tested.register(createProductRequest);

        verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), user.getId());
        verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService).byId(createProductRequest.getCategoryId(), user.getId());
        verify(productRepository).save(productCaptor.capture());

        Product product = productCaptor.getValue();

        assertEquals(productRemoved.getId(), product.getId());
        assertEquals(product.getId(), idResponse.getId());
        assertEquals(category, product.getCategory());
        assertEquals(createProductRequest.getName(), product.getName());
        assertEquals(user, product.getUser());
        assertTrue(product.isActive());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar novo produto com nome igual a nome de produto já cadastrado pelo usuário e ativo no sistema")
    void deveRetornarErroQuandoNomeProdutoJaForCadastradoPeloUsuario() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(ProductRegisteredException.class)
                .when(validateUniqueProductFromUserService).validate(createProductRequest.getName(), user.getId());

        assertThrows(ProductRegisteredException.class, () -> tested.register(createProductRequest));

        verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), user.getId());
        verify(searchProductFromUserService, never()).byNameAndNotActive(createProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService, never()).byId(createProductRequest.getCategoryId(), user.getId());
        verify(productRepository, never()).save(productCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar produto com id de categoria inválida para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalidoParaUsuario() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), user.getId())).thenReturn(null);

        doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(createProductRequest.getCategoryId(), user.getId());

        Assertions.assertThrows(CategoryInvalidException.class, () -> tested.register(createProductRequest));

        verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), user.getId());
        verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), user.getId());
        verify(searchCategoryFromUserService).byId(createProductRequest.getCategoryId(), user.getId());
        verify(productRepository, never()).save(productCaptor.capture());
    }
}