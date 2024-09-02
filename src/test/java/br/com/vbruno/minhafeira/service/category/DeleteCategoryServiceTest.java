package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryServiceTest {

    @InjectMocks
    private DeleteCategoryService tested;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Mock
    private CategoryRepository categoryRepository;

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
    @DisplayName("Deve deletar a categoria com sucesso")
    void deveDeletarCategoria() {
        Long idCategory = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        tested.delete(idCategory);

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
        verify(categoryRepository).deleteById(idCategory);
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar deletar a categoria mas o ID da categoria for inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalido() {
        Long idCategory = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, user.getId());

        assertThrows(CategoryInvalidException.class, () -> tested.delete(idCategory));

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
        verify(categoryRepository, never()).deleteById(idCategory);
    }
}