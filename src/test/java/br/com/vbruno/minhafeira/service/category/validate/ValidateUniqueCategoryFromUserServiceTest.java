package br.com.vbruno.minhafeira.service.category.validate;

import br.com.vbruno.minhafeira.exception.CategoryRegisteredException;
import br.com.vbruno.minhafeira.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

        Mockito.when(categoryRepository.existsByNameIgnoreCaseAndUserId(nameCategory, idUser)).thenReturn(false);

        tested.validate(nameCategory, idUser);

        Mockito.verify(categoryRepository).existsByNameIgnoreCaseAndUserId(nameCategory, idUser);
    }

    @Test
    @DisplayName("Deve retornar erro quando o usuario já tiver cadastrado uma categoria com aquele nome no sistema")
    void deveRetornarErroQuandoCategoriaNaoForUnicaParaUsuario() {

        String nameCategory = "Categoria teste";
        Long idUser = 1L;

        Mockito.when(categoryRepository.existsByNameIgnoreCaseAndUserId(nameCategory, idUser)).thenReturn(true);

        CategoryRegisteredException exception =
                Assertions.assertThrows(CategoryRegisteredException.class, () -> tested.validate(nameCategory, idUser));

        Mockito.verify(categoryRepository).existsByNameIgnoreCaseAndUserId(nameCategory, idUser);

        Assertions.assertEquals("Esta categoria já está cadastrada", exception.getMessage());
    }
}