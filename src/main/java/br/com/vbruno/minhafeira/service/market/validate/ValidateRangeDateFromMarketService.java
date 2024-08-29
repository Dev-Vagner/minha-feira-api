package br.com.vbruno.minhafeira.service.market.validate;

import br.com.vbruno.minhafeira.exception.RangeDateInvalidException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ValidateRangeDateFromMarketService {

    public void validate(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate) || startDate.equals(endDate)) throw new RangeDateInvalidException("Intervalo de datas inválido");
    }
}
