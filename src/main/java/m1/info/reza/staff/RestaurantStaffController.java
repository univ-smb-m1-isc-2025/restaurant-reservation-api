package m1.info.reza.staff;

import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantController;
import m1.info.reza.restaurant.RestaurantService;
import m1.info.reza.staff.DTO.RestaurantStaffDTO;
import m1.info.reza.staff.DTO.RestaurantWithRoleDTO;
import m1.info.reza.staff.roles.DTO.RoleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant/{restaurantId}/staff")
public class RestaurantStaffController {

    private final AuthenticatedUserService authenticatedUserService;
    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantService restaurantService;

    public RestaurantStaffController(AuthenticatedUserService authenticatedUserService, RestaurantStaffService restaurantStaffService, RestaurantService restaurantService) {
        this.authenticatedUserService = authenticatedUserService;
        this.restaurantStaffService = restaurantStaffService;
        this.restaurantService = restaurantService;
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

}
