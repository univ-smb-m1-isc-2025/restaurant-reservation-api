package m1.info.reza.staff;

import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantService;
import m1.info.reza.staff.DTO.CreateStaffRequest;
import m1.info.reza.staff.DTO.RestaurantStaffDTO;
import m1.info.reza.staff.DTO.UpdateStaffRequest;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.staff.roles.RoleService;
import m1.info.reza.user.User;
import m1.info.reza.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RestaurantStaffControllerTest {

    @Mock private AuthenticatedUserService authenticatedUserService;
    @Mock private RestaurantStaffService restaurantStaffService;
    @Mock private RestaurantService restaurantService;
    @Mock private UserService userService;
    @Mock private RoleService roleService;
    @Mock private RestaurantStaffRepository restaurantStaffRepository;

    @InjectMocks private RestaurantStaffController controller;

    private Restaurant restaurant;
    private User user;
    private Role role;
    private RestaurantStaff staff;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        restaurant = new Restaurant();
        restaurant.setId(1L);

        user = new User();
        user.setId(2L);
        user.setEmail("staff@example.com");

        role = new Role();
        role.setId(3L);
        role.setRoleName("WAITER");

        staff = new RestaurantStaff();
        staff.setUser(user);
        staff.setRole(role);
        staff.setRestaurant(restaurant);
    }

    @Test
    public void testGetAllStaff() {
        when(restaurantService.getRestaurant(1L)).thenReturn(restaurant);
        when(restaurantStaffService.getAllStaffFromRestaurant(restaurant)).thenReturn(List.of(staff));

        ResponseEntity<ApiResponse<List<RestaurantStaffDTO>>> response = controller.index(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData()).hasSize(1);
        verify(authenticatedUserService).checkAuthenticatedUserIsStaff(1L);
    }

    @Test
    public void testCreateStaff() {
        CreateStaffRequest request = new CreateStaffRequest();
        request.setUserEmail("staff@example.com");
        request.setRoleId(3L);

        when(restaurantService.getRestaurant(1L)).thenReturn(restaurant);
        when(userService.getUserByEmail("staff@example.com")).thenReturn(user);
        when(roleService.getRole(3L)).thenReturn(role);
        when(restaurantStaffService.getAllStaffFromRestaurant(restaurant)).thenReturn(List.of(staff));

        ResponseEntity<ApiResponse<List<RestaurantStaffDTO>>> response = controller.index(1L, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getData()).hasSize(1);
        verify(restaurantStaffService).create(restaurant, user, role);
        verify(authenticatedUserService).checkAuthenticatedUserRoleManagerOrHigher(1L);
    }

    @Test
    public void testUpdateStaff_success() {
        UpdateStaffRequest request = new UpdateStaffRequest();
        request.setUserId(2L);
        request.setRoleId(4L); // new role

        Role newRole = new Role();
        newRole.setId(4L);
        newRole.setRoleName("MANAGER");

        when(restaurantStaffService.getRestaurantStaff(1L, 2L)).thenReturn(staff);
        when(roleService.getOwnerRole()).thenReturn(new Role()); // ID default = null
        when(roleService.getRole(4L)).thenReturn(newRole);

        RestaurantStaff updatedStaff = new RestaurantStaff();
        updatedStaff.setUser(user);
        updatedStaff.setRole(newRole);
        updatedStaff.setRestaurant(restaurant);

        when(restaurantStaffService.update(staff, newRole)).thenReturn(updatedStaff);

        ResponseEntity<ApiResponse<RestaurantStaffDTO>> response = controller.update(1L, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData().getRole().getRoleName()).isEqualTo("MANAGER");
        verify(authenticatedUserService).checkAuthenticatedUserRoleManagerOrHigher(1L);
    }

    @Test
    public void testUpdateStaff_throwsIfRoleIsSame() {
        UpdateStaffRequest request = new UpdateStaffRequest();
        request.setUserId(2L);
        request.setRoleId(3L); // same as current role ID

        when(restaurantStaffService.getRestaurantStaff(1L, 2L)).thenReturn(staff);
        when(roleService.getOwnerRole()).thenReturn(new Role());
        when(roleService.getRole(3L)).thenReturn(role);

        assertThrows(BadRequestException.class, () -> controller.update(1L, request));
    }

    @Test
    public void testUpdateStaff_throwsIfIsOwner() {
        Role ownerRole = new Role();
        ownerRole.setId(3L); // same ID as current role

        when(restaurantStaffService.getRestaurantStaff(1L, 2L)).thenReturn(staff);
        when(roleService.getOwnerRole()).thenReturn(ownerRole);

        UpdateStaffRequest request = new UpdateStaffRequest();
        request.setUserId(2L);
        request.setRoleId(4L);

        assertThrows(BadRequestException.class, () -> controller.update(1L, request));
    }
}
