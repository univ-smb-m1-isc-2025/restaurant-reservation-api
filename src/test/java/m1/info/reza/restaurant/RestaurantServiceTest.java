package m1.info.reza.restaurant;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.restaurant.DTO.RestaurantCreateRequest;
import m1.info.reza.staff.RestaurantStaffService;
import m1.info.reza.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantStaffService restaurantStaffService;

    @InjectMocks
    private RestaurantService restaurantService;

    private RestaurantCreateRequest restaurantCreateRequest;
    private User creator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        restaurantCreateRequest = new RestaurantCreateRequest("Restaurant A", "123 Main St", "Paris", "75001", 50);
        creator = mock(User.class);
    }

    @Test
    void create_shouldCreateRestaurantAndAddOwner() {
        // Given
        Restaurant newRestaurant = new Restaurant(
                restaurantCreateRequest.getName(),
                restaurantCreateRequest.getAddress(),
                restaurantCreateRequest.getCity(),
                restaurantCreateRequest.getZipcode(),
                restaurantCreateRequest.getCapacity()
        );
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(newRestaurant);

        // When
        Restaurant createdRestaurant = restaurantService.create(restaurantCreateRequest, creator);

        // Then
        assertNotNull(createdRestaurant);
        assertEquals("Restaurant A", createdRestaurant.getName());
        assertEquals("123 Main St", createdRestaurant.getAddress());

        // Verify that the owner has been added
        verify(restaurantStaffService).addOwner(createdRestaurant, creator);
        verify(restaurantRepository).save(createdRestaurant);
    }

    @Test
    void getRestaurant_shouldReturnRestaurant_whenFound() {
        // Given
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant(1L, "Restaurant A", "123 Main St", "Paris", "75001", 50);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // When
        Restaurant result = restaurantService.getRestaurant(restaurantId);

        System.out.println(result.getId());

        // Then
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Restaurant A", result.getName());
    }

    @Test
    void getRestaurant_shouldThrowEntityNotFoundException_whenNotFound() {
        // Given
        Long restaurantId = 999L;
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            restaurantService.getRestaurant(restaurantId);
        });

        assertEquals("Le restaurant avec l'id999 n'existe pas.", exception.getMessage());
    }
}
