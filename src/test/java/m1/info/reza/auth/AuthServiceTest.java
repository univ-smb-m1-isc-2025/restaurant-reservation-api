package m1.info.reza.auth;

import m1.info.reza.auth.DTO.LoginUserDto;
import m1.info.reza.auth.DTO.RegisterUserDto;
import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.user.User;
import m1.info.reza.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_shouldCreateUserSuccessfully() {
        RegisterUserDto input = new RegisterUserDto("test@example.com", "password", "Alice", "Smith");

        when(userRepository.existsByEmail(input.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(input.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = authService.signup(input);

        assertEquals(input.getEmail(), created.getEmail());
        assertEquals("hashedPassword", created.getPassword());
        assertEquals(input.getFirstName(), created.getFirstName());
        assertEquals(input.getLastName(), created.getLastName());
    }

    @Test
    void signup_shouldThrowException_whenEmailExists() {
        RegisterUserDto input = new RegisterUserDto("used@example.com", "password", "Test", "User");

        when(userRepository.existsByEmail(input.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.signup(input));
    }

    @Test
    void authenticate_shouldReturnUser_whenCredentialsAreValid() {
        LoginUserDto login = new LoginUserDto("test@example.com", "password");
        User user = new User(login.getEmail(), "encoded", "Alice", "Smith");

        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("test@example.com", "password"));

        User authenticated = authService.authenticate(login);

        assertEquals(user.getEmail(), authenticated.getEmail());
    }

    @Test
    void authenticate_shouldThrowException_whenUserNotFound() {
        LoginUserDto login = new LoginUserDto("notfound@example.com", "password");

        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(login));
    }

    @Test
    void authenticate_shouldThrowException_whenPasswordInvalid() {
        LoginUserDto login = new LoginUserDto("test@example.com", "wrong");
        User user = new User(login.getEmail(), "encoded", "Alice", "Smith");

        when(userRepository.findByEmail(login.getEmail())).thenReturn(Optional.of(user));
        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(login));
    }
}
