package br.com.vbruno.minhafeira.service.product;

import br.com.vbruno.minhafeira.DTO.response.product.ListProductResponse;
import br.com.vbruno.minhafeira.domain.Product;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.factory.ProductFactory;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.ProductRepository;
import br.com.vbruno.minhafeira.service.user.search.SearchUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListProductServiceTest {

    @InjectMocks
    private ListProductService tested;

    @Mock
    private ProductRepository productRepository;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void beforeTests() {
        securityContextHolderMock = Mockito.mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void afterTests() {
        securityContextHolderMock.close();
    }

    @Test
    @DisplayName("Deve listar todos os produtos ativos do usuário com sucesso")
    void deveListarTodosProdutosDoUsuario() {
        Product product = ProductFactory.getProductWithCategory();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();
        int expectedListSize = 1;

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(productRepository.findAllByUserIdAndActiveTrue(user.getId())).thenReturn(Collections.singletonList(product));

        List<ListProductResponse> listProductReturned = tested.list();

        verify(productRepository).findAllByUserIdAndActiveTrue(user.getId());

        assertEquals(expectedListSize, listProductReturned.size());
        assertEquals(product.getId(), listProductReturned.get(0).getId());
        assertEquals(product.getName(), listProductReturned.get(0).getName());
    }

}