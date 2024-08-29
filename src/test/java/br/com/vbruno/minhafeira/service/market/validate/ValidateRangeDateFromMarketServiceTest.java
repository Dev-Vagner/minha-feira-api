package br.com.vbruno.minhafeira.service.market.validate;

import br.com.vbruno.minhafeira.exception.RangeDateInvalidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class ValidateRangeDateFromMarketServiceTest {

    @InjectMocks
    private ValidateRangeDateFromMarketService tested;

    @Test
    @DisplayName("Não deve fazer nada quando a data inicial passada for uma data anterior a data final")
    void naoDeveFazerNadaQuandoIntervaloDeDatasCorreto() {
        LocalDate startDate = LocalDate.of(2024, 8, 25);
        LocalDate endDate = LocalDate.of(2024, 8, 29);

        tested.validate(startDate, endDate);
    }

    @Test
    @DisplayName("Deve retornar erro quando a data inicial passada for uma data após a data final")
    void deveRetornarErroQuandoDataInicialPosteriorDataFinal() {
        LocalDate startDate = LocalDate.of(2024, 8, 29);
        LocalDate endDate = LocalDate.of(2024, 8, 25);

        RangeDateInvalidException exception =
                Assertions.assertThrows(RangeDateInvalidException.class, () -> tested.validate(startDate, endDate));

        Assertions.assertEquals("Intervalo de datas inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar erro quando a data inicial passada for igual a data final")
    void deveRetornarErroQuandoDataInicialIgualDataFinal() {
        LocalDate startDate = LocalDate.of(2024, 8, 29);
        LocalDate endDate = LocalDate.of(2024, 8, 29);

        RangeDateInvalidException exception =
                Assertions.assertThrows(RangeDateInvalidException.class, () -> tested.validate(startDate, endDate));

        Assertions.assertEquals("Intervalo de datas inválido", exception.getMessage());
    }
}