package br.com.vbruno.minhafeira.service.market.validate;

import br.com.vbruno.minhafeira.exception.ProductMarketNotUniqueException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ValidateUniqueProductFromMarketService {

    public void validate(List<Long> listIdsProducts) {
        int quantityIdUnique = 1;

        Map<Long, Long> idsQuantities = listIdsProducts.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Set<Long> idsDuplicate = idsQuantities.entrySet().stream()
                .filter(idQuantities -> idQuantities.getValue() > quantityIdUnique)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if(!idsDuplicate.isEmpty()) throw new ProductMarketNotUniqueException("Não pode haver produtos repetidos na feira");
    }
}
