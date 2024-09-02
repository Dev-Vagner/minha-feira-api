package br.com.vbruno.minhafeira.service.category.search;

import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCategoryFromUserServiceTest {

    @InjectMocks
    private SearchCategoryFromUserService tested;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Deve retornar categoria quando o ID da categoria for válido para o usuário passado")
    void deveRetornarCategoriaQuandoIdValidoParaUsuario() {
        Category category = CategoryFactory.getCategory();
        Long idUser = 1L;

        when(categoryRepository.findByIdAndUserId(category.getId(), idUser)).thenReturn(Optional.of(category));

        Category categoryReturned = tested.byId(category.getId(), idUser);

        verify(categoryRepository).findByIdAndUserId(category.getId(), idUser);

        assertEquals(category, categoryReturned);
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID da categoria for inválido para o usuário passado")
    void deveRetornarErroQuandoIdCategoriaInvalidoParaUsuario() {
        Long idCategory = 1L;
        Long idUser = 2L;

        CategoryInvalidException exception =
                assertThrows(CategoryInvalidException.class, () -> tested.byId(idCategory, idUser));

        verify(categoryRepository).findByIdAndUserId(idCategory, idUser);

        assertEquals("Categoria inválida", exception.getMessage());
    }
}