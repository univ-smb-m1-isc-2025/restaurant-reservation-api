package m1.info.reza.auth;

import m1.info.reza.auth.DTO.AuthResponseDto;
import m1.info.reza.auth.DTO.LoginUserDto;
import m1.info.reza.auth.DTO.RegisterUserDto;
import m1.info.reza.configs.JwtService;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setEmail("newuser@example.com");
        registerDto.setPassword("securepassword");

        User user = new User();
        user.setEmail("newuser@example.com");

        when(authService.signup(registerDto)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mocked-jwt-token");

        ResponseEntity<ApiResponse<AuthResponseDto>> response = authController.register(registerDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("inscrit");
        assertThat(response.getBody().getData().getToken()).isEqualTo("mocked-jwt-token");
        assertThat(response.getBody().getData().getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    public void testLogin() {
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("existing@example.com");
        loginDto.setPassword("password");

        User user = new User();
        user.setEmail("existing@example.com");

        when(authService.authenticate(loginDto)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt-token-login");

        ResponseEntity<ApiResponse<AuthResponseDto>> response = authController.authenticate(loginDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("connecté");
        assertThat(response.getBody().getData().getToken()).isEqualTo("jwt-token-login");
        assertThat(response.getBody().getData().getEmail()).isEqualTo("existing@example.com");
    }
}

