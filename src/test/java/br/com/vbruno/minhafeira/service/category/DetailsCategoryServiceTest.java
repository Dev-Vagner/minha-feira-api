package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailsCategoryServiceTest {

    @InjectMocks
    private DetailsCategoryService tested;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

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
    @DisplayName("Deve retornar os dados detalhados da categoria com sucesso")
    void deveRetornarDadosDetalhadosCategoria() {
        Long idCategory = 1L;
        Category category = CategoryFactory.getCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(searchCategoryFromUserService.byId(idCategory, user.getId())).thenReturn(category);

        DetailsCategoryResponse detailsCategoryResponse = tested.details(idCategory);

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());

        assertEquals(category.getId(), detailsCategoryResponse.getId());
        assertEquals(category.getName(), detailsCategoryResponse.getName());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar retornar os dados da categoria mas o ID da categoria enviado for inválido para aquele usuário")
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

        assertThrows(CategoryInvalidException.class, () -> tested.details(idCategory));

        verify(searchCategoryFromUserService).byId(idCategory, user.getId());
    }
}