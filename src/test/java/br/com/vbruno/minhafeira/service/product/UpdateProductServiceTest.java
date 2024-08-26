package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.request.product.UpdateProductRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.exception.ProductInvalidException;
import br.com.vbruno.minhafeira.exception.ProductRegisteredException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.product.search.SearchProductFromUserService;
import br.com.vbruno.minhafeira.service.product.validate.ValidateUniqueProductFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    @DisplayName("Deve editar os dados do produto com sucesso quando for passado categoria")
    void deveEditarDadosProdutoComCategoria() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestWithCategory();
        Product product = ProductFactory.getProductWithCategory();
        Category category = CategoryFactory.getCategory();
        Long idProduct = 1L;
        Long idUser = 2L;

        Mockito.when(searchProductFromUserService.byId(idProduct, idUser)).thenReturn(product);
        Mockito.when(searchCategoryFromUserService.byId(updateProductRequest.getCategoryId(), idUser)).thenReturn(category);

        IdResponse idResponse = tested.update(idProduct, idUser, updateProductRequest);

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService).byId(updateProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Product productUpdate = productCaptor.getValue();

        Assertions.assertEquals(productUpdate.getId(), idResponse.getId());
        Assertions.assertEquals(category, productUpdate.getCategory());
        Assertions.assertEquals(updateProductRequest.getName(), productUpdate.getName());
    }

    @Test
    @DisplayName("Deve editar os dados do produto com sucesso quando não for passado categoria")
    void deveEditarDadosProdutoSemCategoria() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestNotCategory();
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        Long idUser = 2L;

        Mockito.when(searchProductFromUserService.byId(idProduct, idUser)).thenReturn(product);

        IdResponse idResponse = tested.update(idProduct, idUser, updateProductRequest);

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService, Mockito.never()).byId(updateProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Product productSaved = productCaptor.getValue();

        Assertions.assertEquals(productSaved.getId(), idResponse.getId());
        Assertions.assertNull(productSaved.getCategory());
        Assertions.assertEquals(updateProductRequest.getName(), productSaved.getName());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar produto mas o ID do produto for inválido para aquele usuário")
    void deveRetornarErroQuandoIdProdutoInvalidoParaUsuario() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestNotCategory();
        Long idProduct = 1L;
        Long idCategory = 2L;
        Long idUser = 3L;

        Mockito.doThrow(ProductInvalidException.class)
                .when(searchProductFromUserService).byId(idProduct, idUser);

        Assertions.assertThrows(ProductInvalidException.class, () -> tested.update(idProduct, idUser, updateProductRequest));

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
        Mockito.verify(validateUniqueProductFromUserService, Mockito.never()).validate(updateProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService, Mockito.never()).byId(idCategory, idUser);
        Mockito.verify(productRepository, Mockito.never()).save(productCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar alterar o nome do produto e já existir um produto cadastrado pelo usuário com o mesmo nome e ativo no sistema")
    void deveRetornarErroQuandoNomeProdutoJaCadastradoPeloUsuario() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestNotCategory();
        Product product = ProductFactory.getProductWithCategory();
        Long idProduct = 1L;
        Long idCategory = 2L;
        Long idUser = 3L;

        Mockito.when(searchProductFromUserService.byId(idProduct, idUser)).thenReturn(product);

        Mockito.doThrow(ProductRegisteredException.class)
                .when(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), idUser);

        Assertions.assertThrows(ProductRegisteredException.class, () -> tested.update(idProduct, idUser, updateProductRequest));

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService, Mockito.never()).byId(idCategory, idUser);
        Mockito.verify(productRepository, Mockito.never()).save(productCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar produto com um ID de categoria inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalidoParaUsuario() {
        UpdateProductRequest updateProductRequest = ProductFactory.getUpdateProductRequestWithCategory();
        Product product = ProductFactory.getProductNotCategory();
        Long idProduct = 1L;
        Long idUser = 3L;

        Mockito.when(searchProductFromUserService.byId(idProduct, idUser)).thenReturn(product);

        Mockito.doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(updateProductRequest.getCategoryId(), idUser);

        Assertions.assertThrows(CategoryInvalidException.class, () -> tested.update(idProduct, idUser, updateProductRequest));

        Mockito.verify(searchProductFromUserService).byId(idProduct, idUser);
        Mockito.verify(validateUniqueProductFromUserService).validate(updateProductRequest.getName(), idUser);
        Mockito.verify(searchCategoryFromUserService).byId(updateProductRequest.getCategoryId(), idUser);
        Mockito.verify(productRepository, Mockito.never()).save(productCaptor.capture());
    }
}






