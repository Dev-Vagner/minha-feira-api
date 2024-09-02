package br.com.vbruno.minhafeira.service.category.validate;

import br.com.vbruno.minhafeira.exception.CategoryRegisteredException;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateUniqueCategoryFromUserServiceTest {

    @InjectMocks
    private ValidateUniqueCategoryFromUserService tested;

    @Mock
    private CategoryRepository categoryRepository;


    @Test
    @DisplayName("Não deve fazer nada quando o usuario ainda não tiver cadastrado uma categoria com aquele nome no sistema")
    void naoDeveFazerNadaQuandoCategoriaForUnicaParaUsuario() {
        String nameCategory = "Categoria teste";
        Long idUser = 1L;

        when(categoryRepository.existsByNameIgnoreCaseAndUserId(nameCategory, idUser)).thenReturn(false);

        tested.validate(nameCategory, idUser);

        verify(categoryRepository).existsByNameIgnoreCaseAndUserId(nameCategory, idUser);
    }

    @Test
    @DisplayName("Deve retornar erro quando o usuario já tiver cadastrado uma categoria com aquele nome no sistema")
    void deveRetornarErroQuandoCategoriaNaoForUnicaParaUsuario() {
        String nameCategory = "Categoria teste";
        Long idUser = 1L;

        when(categoryRepository.existsByNameIgnoreCaseAndUserId(nameCategory, idUser)).thenReturn(true);

        CategoryRegisteredException exception =
                assertThrows(CategoryRegisteredException.class, () -> tested.validate(nameCategory, idUser));

        verify(categoryRepository).existsByNameIgnoreCaseAndUserId(nameCategory, idUser);

        assertEquals("Esta categoria já está cadastrada", exception.getMessage());
    }
}