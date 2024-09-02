package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCategoryServiceTest {

    @InjectMocks
    private ListCategoryService tested;

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
    @DisplayName("Deve listar todas as categorias do usuário com sucesso")
    void deveListarTodasCategoriasDoUsuario() {
        Category category = CategoryFactory.getCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();
        int expectedListSize = 1;

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(categoryRepository.findAllByUserId(user.getId())).thenReturn(Collections.singletonList(category));

        List<ListCategoryResponse> listCategoryReturned = tested.list();

        verify(categoryRepository).findAllByUserId(user.getId());

        assertEquals(expectedListSize, listCategoryReturned.size());
        assertEquals(category.getId(), listCategoryReturned.get(0).getId());
        assertEquals(category.getName(), listCategoryReturned.get(0).getName());
    }
}