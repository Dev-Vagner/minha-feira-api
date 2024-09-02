package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.response.user.DetailsUserResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.factory.UserFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetailsUserServiceTest {

    @InjectMocks
    private DetailsUserService tested;

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
    @DisplayName("Deve retornar os dados detalhados do usuario com sucesso")
    void deveRetornarDadosDetalhadosUsuario() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        DetailsUserResponse detailsUserResponse = tested.details();

        Assertions.assertEquals(user.getId(), detailsUserResponse.getId());
        Assertions.assertEquals(user.getName(), detailsUserResponse.getName());
        Assertions.assertEquals(user.getEmail(), detailsUserResponse.getEmail());
    }
}