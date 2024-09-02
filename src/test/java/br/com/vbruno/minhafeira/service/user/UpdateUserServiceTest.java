package br.com.vbruno.minhafeira.service.user;

import br.com.vbruno.minhafeira.DTO.request.user.UpdateUserRequest;
import br.com.vbruno.minhafeira.DTO.response.IdResponse;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.exception.EmailRegisteredException;
import br.com.vbruno.minhafeira.factory.UserFactory;
import br.com.vbruno.minhafeira.repository.UserRepository;
import br.com.vbruno.minhafeira.service.user.validate.ValidateUniqueEmailUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @InjectMocks
    private UpdateUserService tested;

    @Mock
    private ValidateUniqueEmailUserService validateUniqueEmailUserService;

    @Mock
    private UserRepository userRepository;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void beforeTests() {
        securityContextHolderMock = Mockito.mockStatic(SecurityContextHolder.class);
    }

    @AfterEach
    void afterTests() {
        securityContextHolderMock.close();
    }

    @Test
    @DisplayName("Deve editar os dados do usuário com sucesso")
    void deveEditarDadosUsuario() {
        UpdateUserRequest updateUserRequest = UserFactory.getUpdateUserRequest();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        IdResponse idResponse = tested.update(updateUserRequest);

        Mockito.verify(validateUniqueEmailUserService).validate(updateUserRequest.getEmail());
        Mockito.verify(userRepository).save(userCaptor.capture());

        User userSaved = userCaptor.getValue();

        Assertions.assertEquals(userSaved.getId(), idResponse.getId());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar alterar email e o email já estiver cadastrado no sistema")
    void deveRetornarErroQuandoEmailJaCadastradoSistema() {
        UpdateUserRequest updateUserRequest = UserFactory.getUpdateUserRequest();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = UserFactory.getUser();

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        Mockito.doThrow(EmailRegisteredException.class)
                .when(validateUniqueEmailUserService).validate(updateUserRequest.getEmail());

        Assertions.assertThrows(EmailRegisteredException.class, () -> tested.update(updateUserRequest));

        Mockito.verify(validateUniqueEmailUserService).validate(updateUserRequest.getEmail());
        Mockito.verify(userRepository, Mockito.never()).save(userCaptor.capture());
    }
}