package m1.info.reza.restaurant;

import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.restaurant.DTO.RestaurantCreateRequest;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.staff.DTO.RestaurantWithRoleDTO;
import m1.info.reza.staff.RestaurantStaff;
import m1.info.reza.staff.RestaurantStaffService;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RestaurantControllerTest {

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private RestaurantStaffService restaurantStaffService;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
    }

    @Test
    public void testCreateRestaurant() {
        RestaurantCreateRequest request = new RestaurantCreateRequest();
        request.setName("Test Resto");
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Resto");

        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(testUser);
        when(restaurantService.create(request, testUser)).thenReturn(restaurant);

        ResponseEntity<ApiResponse<Restaurant>> response = restaurantController.create(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getName()).isEqualTo("Test Resto");
    }

    @Test
    public void testGetMyRestaurants() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Chez Code");

        Role role = new Role();
        role.setRoleName("MANAGER");

        RestaurantStaff staff = new RestaurantStaff();
        staff.setRestaurant(restaurant);
        staff.setRole(role);

        when(authenticatedUserService.getAuthenticatedUser()).thenReturn(testUser);
        when(restaurantStaffService.getRestaurantsForUser(testUser)).thenReturn(List.of(staff));

        ResponseEntity<ApiResponse<List<RestaurantWithRoleDTO>>> response = restaurantController.index();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData()).hasSize(1);
    }

    @Test
    public void testGetRestaurantById() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Resto Unique");

        when(restaurantService.getRestaurant(1L)).thenReturn(restaurant);

        ResponseEntity<ApiResponse<RestaurantDTO>> response = restaurantController.index(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        RestaurantDTO dto = response.getBody().getData();
        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("Resto Unique");
    }

}
