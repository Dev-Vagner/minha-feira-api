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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @DisplayName("Deve cadastrar uma nova categoria com sucesso")
    void deveCadastrarNovaCategoria() {
        CreateCategoryRequest createCategoryRequest = CategoryFactory.getCreateCategoryRequest();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        IdResponse idResponse = tested.register(createCategoryRequest);

        Mockito.verify(validateUniqueCategoryFromUserService).validate(createCategoryRequest.getName(), user.getId());
        Mockito.verify(categoryRepository).save(categoryCaptor.capture());

        Category category = categoryCaptor.getValue();

        Assertions.assertEquals(category.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar cadastrar categoria com nome já cadastrado pelo usuário no sistema")
    void deveRetornarErroQuandoNomeCategoriaJaForCadastradoPeloUsuario() {
        CreateCategoryRequest createCategoryRequest = CategoryFactory.getCreateCategoryRequest();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        Mockito.doThrow(CategoryRegisteredException.class)
                .when(validateUniqueCategoryFromUserService).validate(createCategoryRequest.getName(), user.getId());

        Assertions.assertThrows(CategoryRegisteredException.class, () -> tested.register(createCategoryRequest));

        Mockito.verify(validateUniqueCategoryFromUserService).validate(createCategoryRequest.getName(), user.getId());
        Mockito.verify(categoryRepository, Mockito.never()).save(categoryCaptor.capture());
    }
}