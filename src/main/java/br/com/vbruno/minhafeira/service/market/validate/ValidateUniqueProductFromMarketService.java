package br.com.vbruno.minhafeira.service.market.validate;

import br.com.vbruno.minhafeira.exception.ProductNotUniqueMarketException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ValidateUniqueProductFromMarketService {

    public void validate(List<Long> listIdsProducts) {
        Map<Long, Long> idsQuantities = getIdsQuantities(listIdsProducts);

        Set<Long> idsDuplicate = getIdsDuplicate(idsQuantities);

        if(!idsDuplicate.isEmpty()) throw new ProductNotUniqueMarketException("Não pode haver produtos repetidos na feira");
    }

    private Map<Long, Long> getIdsQuantities(List<Long> listIdsProducts) {
        return listIdsProducts.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private Set<Long> getIdsDuplicate(Map<Long, Long> idsQuantities) {
        int quantityIdUnique = 1;

        return idsQuantities.entrySet().stream()
                .filter(idQuantities -> idQuantities.getValue() > quantityIdUnique)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
