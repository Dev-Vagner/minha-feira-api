package br.com.vbruno.minhafeira.service.category.search;

import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

        Mockito.when(categoryRepository.findByIdAndUserId(category.getId(), idUser)).thenReturn(Optional.of(category));

        Category categoryReturned = tested.byId(category.getId(), idUser);

        Mockito.verify(categoryRepository).findByIdAndUserId(category.getId(), idUser);

        Assertions.assertEquals(category, categoryReturned);
    }

    @Test
    @DisplayName("Deve retornar erro quando o ID da categoria for inválido para o usuário passado")
    void deveRetornarErroQuandoIdInvalidoParaUsuario() {
        Long idCategory = 1L;
        Long idUser = 2L;

        CategoryInvalidException exception =
                Assertions.assertThrows(CategoryInvalidException.class, () -> tested.byId(idCategory, idUser));

        Mockito.verify(categoryRepository).findByIdAndUserId(idCategory, idUser);

        Assertions.assertEquals("Categoria inválida", exception.getMessage());
    }
}