package m1.info.reza.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // Test for getUserByEmail when user exists
    @Test
    void getUserByEmail_shouldReturnUser_whenUserExists() {
        // Given
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    // Test for getUserByEmail when user does not exist
    @Test
    void getUserByEmail_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        // Given
        String email = "nonexistentuser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserByEmail(email);
        });

        assertEquals("L'utilisateur avec l'email " + email +" n'existe pas.", exception.getMessage());
    }
}

