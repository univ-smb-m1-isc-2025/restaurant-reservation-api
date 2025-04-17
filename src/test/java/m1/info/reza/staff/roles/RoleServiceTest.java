package m1.info.reza.staff.roles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    // Test for getRole
    @Test
    void getRole_shouldReturnRole_whenRoleExists() {
        // Given
        Long roleId = 1L;
        Role role = new Role(1L,"EMPLOYEE");
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // When
        Role result = roleService.getRole(roleId);

        // Then
        assertNotNull(result);
        assertEquals(roleId, result.getId());
    }

    @Test
    void getRole_shouldThrowEntityNotFoundException_whenRoleDoesNotExist() {
        // Given
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roleService.getRole(roleId);
        });

        assertEquals("Le rôle avec l'id" + roleId +" n'existe pas.", exception.getMessage());
    }

    // Test for getRoles
    @Test
    void getRoles_shouldReturnListOfRoles() {
        // Given
        Role role1 = new Role("EMPLOYEE");
        Role role2 = new Role("MANAGER");
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        // When
        List<Role> roles = roleService.getRoles();

        // Then
        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    // Test for getOwnerRole
    @Test
    void getOwnerRole_shouldReturnOwnerRole_whenExists() {
        // Given
        Role ownerRole = new Role("OWNER");
        when(roleRepository.findByRoleName("OWNER")).thenReturn(Optional.of(ownerRole));

        // When
        Role result = roleService.getOwnerRole();

        // Then
        assertNotNull(result);
        assertEquals("OWNER", result.getRoleName());
    }

    @Test
    void getOwnerRole_shouldThrowEntityNotFoundException_whenRoleDoesNotExist() {
        // Given
        when(roleRepository.findByRoleName("OWNER")).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            roleService.getOwnerRole();
        });

        assertEquals("Le rôle 'OWNER' n'existe pas en base de données !", exception.getMessage());
    }

}
