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
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    @InjectMocks
    private CreateProductService tested;

    @Mock
    private ValidateUniqueProductFromUserService validateUniqueProductFromUserService;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Mock
    private SearchProductFromUserService searchProductFromUserService;

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productCaptor;


    @Test
    @DisplayName("Deve cadastrar um novo produto com categoria com sucesso")
    void deveCadastrarNovoProdutoComCategoria() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        Category category = CategoryFactory.getCategory();
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);
        Mockito.when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), idUser)).thenReturn(null);
        Mockito.when(searchCategoryFromUserService.byId(createProductRequest.getCategoryId(), idUser)).thenReturn(category);

        IdResponse idResponse = tested.register(idUser, createProductRequest);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), idUser);
        Mockito.verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService).byId(createProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Product product = productCaptor.getValue();

        Assertions.assertEquals(product.getId(), idResponse.getId());
        Assertions.assertEquals(category, product.getCategory());
        Assertions.assertEquals(createProductRequest.getName(), product.getName());
        Assertions.assertEquals(user, product.getUser());
        Assertions.assertTrue(product.isActive());
    }

    @Test
    @DisplayName("Deve cadastrar um novo produto sem categoria com sucesso")
    void deveCadastrarNovoProdutoSemCategoria() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestNotCategory();
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);
        Mockito.when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), idUser)).thenReturn(null);

        IdResponse idResponse = tested.register(idUser, createProductRequest);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), idUser);
        Mockito.verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService, Mockito.never()).byId(createProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Product product = productCaptor.getValue();

        Assertions.assertEquals(product.getId(), idResponse.getId());
        Assertions.assertNull(product.getCategory());
        Assertions.assertEquals(createProductRequest.getName(), product.getName());
        Assertions.assertEquals(user, product.getUser());
        Assertions.assertTrue(product.isActive());
    }

    @Test
    @DisplayName("Deve reativar produto, ao invés de criar um novo, quando o nome de um produto que tenha sido removido for igual ao nome do produto que o usuário deseja cadastrar")
    void deveReativarProdutoERetornarIdProdutoIgualIdProdutoRemovido() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        Category category = CategoryFactory.getCategory();
        Long idProductRemoved = 10L;
        Product productRemoved = ProductFactory.getProductNotActive();
        productRemoved.setId(idProductRemoved);
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);
        Mockito.when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), idUser)).thenReturn(productRemoved);
        Mockito.when(searchCategoryFromUserService.byId(createProductRequest.getCategoryId(), idUser)).thenReturn(category);

        IdResponse idResponse = tested.register(idUser, createProductRequest);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), idUser);
        Mockito.verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService).byId(createProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Product product = productCaptor.getValue();

        Assertions.assertEquals(productRemoved.getId(), product.getId());
        Assertions.assertEquals(product.getId(), idResponse.getId());
        Assertions.assertEquals(category, product.getCategory());
        Assertions.assertEquals(createProductRequest.getName(), product.getName());
        Assertions.assertEquals(user, product.getUser());
        Assertions.assertTrue(product.isActive());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar novo produto com nome igual a nome de produto já cadastrado pelo usuário e ativo no sistema")
    void deveRetornarErroQuandoNomeProdutoJaForCadastradoPeloUsuario() {
        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);

        Mockito.doThrow(ProductRegisteredException.class)
                .when(validateUniqueProductFromUserService).validate(createProductRequest.getName(), idUser);

        Assertions.assertThrows(ProductRegisteredException.class, () -> tested.register(idUser, createProductRequest));

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), idUser);
        Mockito.verify(searchProductFromUserService, Mockito.never()).byNameAndNotActive(createProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService, Mockito.never()).byId(createProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository, Mockito.never()).save(productCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar produto com id de categoria inválida para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalidoParaUsuario() {


        CreateProductRequest createProductRequest = ProductFactory.getCreateProductRequestWithCategory();
        Long idCategory = 1L;
        User user = UserFactory.getUser();
        Long idUser = 2L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);
        Mockito.when(searchProductFromUserService.byNameAndNotActive(createProductRequest.getName(), idUser)).thenReturn(null);

        Mockito.doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, idUser);

        Assertions.assertThrows(CategoryInvalidException.class, () -> tested.register(idUser, createProductRequest));

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(createProductRequest.getName(), idUser);
        Mockito.verify(searchProductFromUserService).byNameAndNotActive(createProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService).byId(createProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository, Mockito.never()).save(productCaptor.capture());
    }
}