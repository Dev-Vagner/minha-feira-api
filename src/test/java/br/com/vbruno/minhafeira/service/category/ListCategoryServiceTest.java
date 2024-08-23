package br.com.vbruno.minhafeira.service.category;

import br.com.vbruno.minhafeira.DTO.response.category.ListCategoryResponse;
import br.com.vbruno.minhafeira.domain.Category;
import br.com.vbruno.minhafeira.factory.CategoryFactory;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ListCategoryServiceTest {

    @InjectMocks
    private ListCategoryService tested;

    @Mock
    private SearchUserService searchUserService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Deve listar todas as categorias do usuário com sucesso")
    void deveListarTodasCategoriasDoUsuario() {
        Long idUser = 1L;
        Category category = CategoryFactory.getCategory();
        int expectedListSize = 1;

        Mockito.when(categoryRepository.findAllByUserId(idUser)).thenReturn(Collections.singletonList(category));

        List<ListCategoryResponse> listCategoryReturned = tested.list(idUser);

        Mockito.verify(searchUserService).byId(idUser);
        Mockito.verify(categoryRepository).findAllByUserId(idUser);

        Assertions.assertEquals(expectedListSize, listCategoryReturned.size());
        Assertions.assertEquals(category.getId(), listCategoryReturned.get(0).getId());
        Assertions.assertEquals(category.getName(), listCategoryReturned.get(0).getName());
    }
}