package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.exception.CategoryRegisteredException;
import br.com.vbruno.minhafeira.exception.EmailRegisteredException;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.category.validate.ValidateUniqueCategoryFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryServiceTest {

    @InjectMocks
    private UpdateCategoryService tested;

    @Mock
    private ValidateUniqueCategoryFromUserService validateUniqueCategoryFromUserService;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Mock
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @Test
    @DisplayName("Deve editar os dados da categoria com sucesso")
    void deveEditarDadosCategoria() {
        Long idCategory = 1L;
        Long idUser = 2L;
        UpdateCategoryRequest updateCategoryRequest = CategoryFactory.getUpdateCategoryRequest();
        Category category = CategoryFactory.getCategory();

        Mockito.when(searchCategoryFromUserService.byId(idCategory, idUser)).thenReturn(category);

        IdResponse idResponse = tested.update(idCategory, idUser, updateCategoryRequest);

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
        Mockito.verify(validateUniqueCategoryFromUserService).validate(updateCategoryRequest.getName(), idUser);
        Mockito.verify(categoryRepository).save(categoryCaptor.capture());

        Category categorySaved = categoryCaptor.getValue();

        Assertions.assertEquals(categorySaved.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar categoria mas o ID da categoria for inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalidoParaUsuario() {
        Long idCategory = 1L;
        Long idUser = 2L;
        UpdateCategoryRequest updateCategoryRequest = CategoryFactory.getUpdateCategoryRequest();

        Mockito.doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, idUser);

        Assertions.assertThrows(CategoryInvalidException.class, () -> tested.update(idCategory, idUser, updateCategoryRequest));

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
        Mockito.verify(validateUniqueCategoryFromUserService, Mockito.never()).validate(updateCategoryRequest.getName(), idUser);
        Mockito.verify(categoryRepository, Mockito.never()).save(categoryCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar alterar o nome da categoria e esse nome já estiver cadastrado no sistema pelo usuário")
    void deveRetornarErroQuandoNomeCategoriaJaCadastradoPeloUsuario() {
        Long idCategory = 1L;
        Long idUser = 2L;
        UpdateCategoryRequest updateCategoryRequest = CategoryFactory.getUpdateCategoryRequest();
        Category category = CategoryFactory.getCategory();

        Mockito.when(searchCategoryFromUserService.byId(idCategory, idUser)).thenReturn(category);

        Mockito.doThrow(CategoryRegisteredException.class)
                .when(validateUniqueCategoryFromUserService).validate(updateCategoryRequest.getName(), idUser);

        Assertions.assertThrows(CategoryRegisteredException.class, () -> tested.update(idCategory, idUser, updateCategoryRequest));

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
        Mockito.verify(validateUniqueCategoryFromUserService).validate(updateCategoryRequest.getName(), idUser);
        Mockito.verify(categoryRepository, Mockito.never()).save(categoryCaptor.capture());
    }
}