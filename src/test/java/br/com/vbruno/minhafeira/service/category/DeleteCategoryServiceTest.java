package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryServiceTest {

    @InjectMocks
    private DeleteCategoryService tested;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Deve deletar a categoria com sucesso")
    void deveDeletarCategoria() {
        Long idCategory = 1L;
        Long idUser = 2L;

        tested.delete(idCategory, idUser);

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
        Mockito.verify(categoryRepository).deleteById(idCategory);
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar deletar a categoria mas o ID da categoria for inválido para aquele usuário")
    void deveRetornarErroQuandoIdCategoriaInvalido() {
        Long idCategory = 1L;
        Long idUser = 2L;

        Mockito.doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, idUser);

        Assertions.assertThrows(CategoryInvalidException.class, () -> tested.delete(idCategory, idUser));

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
        Mockito.verify(categoryRepository, Mockito.never()).deleteById(idCategory);
    }
}