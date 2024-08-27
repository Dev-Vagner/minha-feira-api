package br.com.vbruno.minhafeira.service.market.validate;

import br.com.vbruno.minhafeira.exception.ProductMarketNotUniqueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ValidateUniqueProductFromMarketServiceTest {

    @InjectMocks
    private ValidateUniqueProductFromMarketService tested;

    @Test
    @DisplayName("Não deve fazer nada quando o usuario enviar uma lista de id's de produtos não duplicados")
    void naoDeveFazerNadaQuandoNaoHouverIdsProdutosDuplicados() {
        List<Long> listIdsProductsNotDuplicate = List.of(1L, 2L, 3L, 4L);

        tested.validate(listIdsProductsNotDuplicate);
    }

    @Test
    @DisplayName("Deve retornar erro quando o usuario enviar uma lista de id's de produtos duplicados")
    void deveRetornarErroQuandoHouverIdsProdutosDuplicados() {
        List<Long> listIdsProductsDuplicate = List.of(1L, 2L, 3L, 1L);

        ProductMarketNotUniqueException exception =
                Assertions.assertThrows(ProductMarketNotUniqueException.class, () -> tested.validate(listIdsProductsDuplicate));

        Assertions.assertEquals("Não pode haver produtos repetidos na feira", exception.getMessage());
    }
}