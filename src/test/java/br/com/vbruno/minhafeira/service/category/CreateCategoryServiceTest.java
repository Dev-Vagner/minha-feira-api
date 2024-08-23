package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.request.category.CreateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.CategoryRegisteredException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.validate.ValidateUniqueCategoryFromUserService;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCategoryServiceTest {

    @InjectMocks
    private CreateCategoryService tested;

    @Mock
    private ValidateUniqueCategoryFromUserService validateUniqueCategoryFromUserService;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private CategoryRepository categoryRepository;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @Test
    @DisplayName("Deve cadastrar uma nova categoria com sucesso")
    void deveCadastrarNovaCategoria() {
        CreateCategoryRequest createCategoryRequest = CategoryFactory.getCreateCategoryRequest();
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);

        IdResponse idResponse = tested.register(idUser, createCategoryRequest);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueCategoryFromUserService).validate(createCategoryRequest.getName(), idUser);
        Mockito.verify(categoryRepository).save(categoryCaptor.capture());

        Category category = categoryCaptor.getValue();

        Assertions.assertEquals(category.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar categoria com nome já cadastrado pelo usuário no sistema")
    void deveRetornarErroQuandoNomeCategoriaJaForCadastradoPeloUsuario() {
        CreateCategoryRequest createCategoryRequest = CategoryFactory.getCreateCategoryRequest();
        User user = UserFactory.getUser();
        Long idUser = 1L;

        Mockito.when(searchUserService.byId(idUser)).thenReturn(user);

        Mockito.doThrow(CategoryRegisteredException.class)
                .when(validateUniqueCategoryFromUserService).validate(createCategoryRequest.getName(), idUser);

        Assertions.assertThrows(CategoryRegisteredException.class, () -> tested.register(idUser, createCategoryRequest));

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(validateUniqueCategoryFromUserService).validate(createCategoryRequest.getName(), idUser);
        Mockito.verify(categoryRepository, Mockito.never()).save(categoryCaptor.capture());
    }
}