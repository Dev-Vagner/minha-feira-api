package br.com.vbruno.minhafeira.service.product.validate;

import br.com.vbruno.minhafeira.exception.ProductRegisteredException;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidateUniqueProductFromUserServiceTest {

    @InjectMocks
    private ValidateUniqueProductFromUserService tested;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("Não deve fazer nada quando o usuario ainda não tiver cadastrado um produto com aquele nome no sistema")
    void naoDeveFazerNadaQuandoProdutoForUnicoParaUsuario() {
        String nameProduct = "Produto teste";
        Long idUser = 1L;

        Mockito.when(productRepository.existsByNameIgnoreCaseAndUserIdAndActiveTrue(nameProduct, idUser)).thenReturn(false);

        tested.validate(nameProduct, idUser);

        Mockito.verify(productRepository).existsByNameIgnoreCaseAndUserIdAndActiveTrue(nameProduct, idUser);
    }

    @Test
    @DisplayName("Deve retornar erro quando o usuario já tiver cadastrado um produto com aquele nome no sistema")
    void deveRetornarErroQuandoProdutoNaoForUnicoParaUsuario() {
        String nameProduct = "Produto teste";
        Long idUser = 1L;

        Mockito.when(productRepository.existsByNameIgnoreCaseAndUserIdAndActiveTrue(nameProduct, idUser)).thenReturn(true);

        ProductRegisteredException exception =
                Assertions.assertThrows(ProductRegisteredException.class, () -> tested.validate(nameProduct, idUser));

        Mockito.verify(productRepository).existsByNameIgnoreCaseAndUserIdAndActiveTrue(nameProduct, idUser);

        Assertions.assertEquals("Este produto já está cadastrado", exception.getMessage());
    }
}