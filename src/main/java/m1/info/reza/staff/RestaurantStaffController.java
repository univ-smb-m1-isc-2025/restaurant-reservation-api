package m1.info.reza.staff;

import jakarta.validation.Valid;
import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantController;
import m1.info.reza.restaurant.RestaurantService;
import m1.info.reza.staff.DTO.CreateStaffRequest;
import m1.info.reza.staff.DTO.RestaurantStaffDTO;
import m1.info.reza.staff.DTO.RestaurantWithRoleDTO;
import m1.info.reza.staff.roles.DTO.RoleDTO;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.staff.roles.RoleService;
import m1.info.reza.user.User;
import m1.info.reza.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/{restaurantId}/staff")
public class RestaurantStaffController {

    private final AuthenticatedUserService authenticatedUserService;
    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final RoleService roleService;

    public RestaurantStaffController(AuthenticatedUserService authenticatedUserService, RestaurantStaffService restaurantStaffService, RestaurantService restaurantService, UserService userService, RoleService roleService) {
        this.authenticatedUserService = authenticatedUserService;
        this.restaurantStaffService = restaurantStaffService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/all")
    private ResponseEntity<ApiResponse<List<RestaurantStaffDTO>>> index(@PathVariable Long restaurantId){
        authenticatedUserService.checkAuthenticatedUserIsStaff(restaurantId);

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        List<RestaurantStaff> staff = restaurantStaffService.getRestaurantStaff(restaurant);

        List<RestaurantStaffDTO> staffDTOs = staff.stream()
                .map(staffMember ->
                        new RestaurantStaffDTO(staffMember.getUser(), staffMember.getRole())
                )
                .toList();

        ApiResponse<List<RestaurantStaffDTO>> response = ResponseUtil.success("La liste du staff du restaurant a été trouvée avec succès.", staffDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    private ResponseEntity<ApiResponse<List<RestaurantStaffDTO>>> index(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateStaffRequest request
    ){
        authenticatedUserService.checkAuthenticatedUserRoleManagerOrHigher(restaurantId);

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        User user = userService.getUserByEmail(request.getUserEmail());
        Role role = roleService.getRole(request.getRoleId());

        restaurantStaffService.addStaff(restaurant, user, role);
        List<RestaurantStaff> staff = restaurantStaffService.getRestaurantStaff(restaurant);

        List<RestaurantStaffDTO> staffDTOs = staff.stream()
                .map(staffMember ->
                        new RestaurantStaffDTO(staffMember.getUser(), staffMember.getRole())
                )
                .toList();

        ApiResponse<List<RestaurantStaffDTO>> response = ResponseUtil.success("L'utilisateur a été ajouté au staff avec succès.", staffDTOs);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
