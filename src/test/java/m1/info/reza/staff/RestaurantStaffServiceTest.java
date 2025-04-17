package m1.info.reza.staff;

import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantRepository;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.staff.roles.RoleService;
import m1.info.reza.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RestaurantStaffServiceTest {

    @Mock
    private RestaurantStaffRepository restaurantStaffRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantStaffService restaurantStaffService;

    private Restaurant restaurant;
    private User user;
    private Role role;
    private RestaurantStaff restaurantStaff;

    @BeforeEach
    void setUp() {
        // Setup mock objects
        restaurant = new Restaurant("Restaurant A", "123 Main St", "Paris", "75001", 50);
        user = new User("john@example.com", "fakepassword", "John", "Doe");
        role = new Role("WAITER"); // Assuming 'WAITER' is a valid role
        restaurantStaff = new RestaurantStaff(user, restaurant, role);
    }

    @Test
    void addOwner_shouldSaveRestaurantStaff_whenRoleIsOwner() {
        // Given
        when(roleService.getOwnerRole()).thenReturn(new Role("OWNER"));

        // When
        restaurantStaffService.addOwner(restaurant, user);

        // Then
        verify(restaurantStaffRepository, times(1)).save(any(RestaurantStaff.class));
    }

    @Test
    void create_shouldThrowBadRequestException_whenRoleIsOwner() {
        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            restaurantStaffService.create(restaurant, user, new Role("OWNER"));
        });

        assertEquals("Vous ne pouvez pas ajouter un utilisateur au rôle d'OWNER.", exception.getMessage());
    }

    @Test
    void create_shouldThrowBadRequestException_whenUserAlreadyPartOfStaff() {
        // Given
        when(restaurantStaffRepository.findByUserIdAndRestaurantId(user.getId(), restaurant.getId()))
                .thenReturn(Optional.of(restaurantStaff));

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            restaurantStaffService.create(restaurant, user, new Role("WAITER"));
        });

        assertEquals("Vous ne pouvez pas ajouter un utilisateur au staff si il en fait déjà partie.", exception.getMessage());
    }

    @Test
    void create_shouldSaveRestaurantStaff_whenRoleIsNotOwner() {
        // Given
        when(restaurantStaffRepository.findByUserIdAndRestaurantId(user.getId(), restaurant.getId()))
                .thenReturn(Optional.empty());

        // When
        RestaurantStaff result = restaurantStaffService.create(restaurant, user, role);

        // Then
        assertNotNull(result);
        verify(restaurantStaffRepository, times(1)).save(result);
    }

    @Test
    void update_shouldThrowBadRequestException_whenRoleIsOwner() {
        // Given
        RestaurantStaff staffToUpdate = new RestaurantStaff(user, restaurant, role);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            restaurantStaffService.update(staffToUpdate, new Role("OWNER"));
        });

        assertEquals("Vous ne pouvez définir un utilisateur au rôle d'OWNER.", exception.getMessage());
    }

    @Test
    void update_shouldUpdateRestaurantStaff_whenRoleIsNotOwner() {
        // Given
        RestaurantStaff staffToUpdate = new RestaurantStaff(user, restaurant, role);
        Role newRole = new Role("CHEF");
        when(restaurantStaffRepository.save(staffToUpdate)).thenReturn(staffToUpdate);

        // When
        RestaurantStaff updatedStaff = restaurantStaffService.update(staffToUpdate, newRole);

        // Then
        assertNotNull(updatedStaff);
        assertEquals("CHEF", updatedStaff.getRole().getRoleName());
        verify(restaurantStaffRepository, times(1)).save(staffToUpdate);
    }

    @Test
    void getRestaurantsForUser_shouldReturnListOfStaff_whenUserHasStaff() {
        // Given
        when(restaurantStaffRepository.findByUser(user)).thenReturn(List.of(restaurantStaff));

        // When
        List<RestaurantStaff> result = restaurantStaffService.getRestaurantsForUser(user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllStaffFromRestaurant_shouldReturnListOfStaff_whenRestaurantHasStaff() {
        // Given
        when(restaurantStaffRepository.findByRestaurant(restaurant)).thenReturn(List.of(restaurantStaff));

        // When
        List<RestaurantStaff> result = restaurantStaffService.getAllStaffFromRestaurant(restaurant);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getRestaurantStaff_shouldReturnRestaurantStaff_whenFound() {
        // Given
        when(restaurantStaffRepository.findByUserIdAndRestaurantId(user.getId(), restaurant.getId()))
                .thenReturn(Optional.of(restaurantStaff));

        // When
        RestaurantStaff result = restaurantStaffService.getRestaurantStaff(restaurant.getId(), user.getId());

        // Then
        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
    }

    @Test
    void getRestaurantStaff_shouldThrowEntityNotFoundException_whenNotFound() {
        // Given
        when(restaurantStaffRepository.findByUserIdAndRestaurantId(user.getId(), restaurant.getId()))
                .thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            restaurantStaffService.getRestaurantStaff(restaurant.getId(), user.getId());
        });

        assertEquals("Le membre du staff avec l'id " + user.getId() + " n'existe pas.", exception.getMessage());
    }
}
