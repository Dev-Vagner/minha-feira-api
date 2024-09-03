package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.mapper.product.ListProductMapper;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ListProductResponse> list() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return productRepository.findAllByUserIdAndActiveTrue(user.getId()).stream()
                .map(ListProductMapper::toResponse)
                .toList();
    }
}
