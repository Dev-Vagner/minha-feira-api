package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.DetailsCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.exception.CategoryInvalidException;
import br.com.vbruno.minhafeira.exception.UserNotRegisteredException;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.service.category.search.SearchCategoryFromUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DetailsCategoryServiceTest {

    @InjectMocks
    private DetailsCategoryService tested;

    @Mock
    private SearchCategoryFromUserService searchCategoryFromUserService;

    @Test
    @DisplayName("Deve retornar os dados detalhados da categoria com sucesso")
    void deveRetornarDadosDetalhadosCategoria() {
        Long idCategory = 1L;
        Long idUser = 2L;
        Category category = CategoryFactory.getCategory();

        Mockito.when(searchCategoryFromUserService.byId(idCategory, idUser)).thenReturn(category);

        DetailsCategoryResponse detailsCategoryResponse = tested.details(idCategory, idUser);

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);

        Assertions.assertEquals(category.getId(), detailsCategoryResponse.getId());
        Assertions.assertEquals(category.getName(), detailsCategoryResponse.getName());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar retornar os dados da categoria mas o ID da categoria enviado for inválido para aquele usuário")
    void deveRetornarErroQuandoIdInvalido() {
        Long idCategory = 1L;
        Long idUser = 2L;

        Mockito.doThrow(CategoryInvalidException.class)
                .when(searchCategoryFromUserService).byId(idCategory, idUser);

        Assertions.assertThrows(CategoryInvalidException.class, () -> tested.details(idCategory, idUser));

        Mockito.verify(searchCategoryFromUserService).byId(idCategory, idUser);
    }
}