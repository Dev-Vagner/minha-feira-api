package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.request.category.UpdateCategoryRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.exception.CategoryRegisteredException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import br.com.vbruno.minhafeira.service.category.validate.ValidateUniqueCategoryFromUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    @DisplayName("Deve editar os dados da categoria com sucesso")
    void deveEditarDadosCategoria() {
        UpdateCategoryRequest updateCategoryRequest = CategoryFactory.getUpdateCategoryRequest();
        Category category = CategoryFactory.getCategory();
        Long idCategory = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchCategoryFromUserService.byId(idCategory, user.getId())).thenReturn(category);

        IdResponse idResponse = tested.update(idCategory, updateCategoryRequest);

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
        verify(validateUniqueCategoryFromUserService).validate(updateCategoryRequest.getName(), user.getId());
        verify(categoryRepository).save(categoryCaptor.capture());

        Category categorySaved = categoryCaptor.getValue();

        assertEquals(categorySaved.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar editar categoria mas o ID da categoria for inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalidoParaUsuario() {
        UpdateCategoryRequest updateCategoryRequest = CategoryFactory.getUpdateCategoryRequest();
        Long idCategory = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, user.getId());

        assertThrows(CategoryInvalidException.class, () -> tested.update(idCategory, updateCategoryRequest));

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
        verify(validateUniqueCategoryFromUserService, never()).validate(updateCategoryRequest.getName(), user.getId());
        verify(categoryRepository, never()).save(categoryCaptor.capture());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar alterar o nome da categoria e esse nome já estiver cadastrado no sistema pelo usuário")
    void deveRetornarErroQuandoNomeCategoriaJaCadastradoPeloUsuario() {
        UpdateCategoryRequest updateCategoryRequest = CategoryFactory.getUpdateCategoryRequest();
        Category category = CategoryFactory.getCategory();
        Long idCategory = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchCategoryFromUserService.byId(idCategory, user.getId())).thenReturn(category);

        doThrow(CategoryRegisteredException.class)
                .when(validateUniqueCategoryFromUserService).validate(updateCategoryRequest.getName(), user.getId());

        assertThrows(CategoryRegisteredException.class, () -> tested.update(idCategory, updateCategoryRequest));

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
        verify(validateUniqueCategoryFromUserService).validate(updateCategoryRequest.getName(), user.getId());
        verify(categoryRepository, never()).save(categoryCaptor.capture());
    }
}