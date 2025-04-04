package m1.info.reza.restaurant;

import jakarta.validation.Valid;
import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.DTO.RestaurantCreateRequest;
import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.staff.DTO.RestaurantWithRoleDTO;
import m1.info.reza.staff.RestaurantStaff;
import m1.info.reza.staff.RestaurantStaffService;
import m1.info.reza.staff.roles.DTO.RoleDTO;
import m1.info.reza.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final AuthenticatedUserService authenticatedUserService;
    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantService restaurantService;

    public RestaurantController(AuthenticatedUserService authenticatedUserService, RestaurantStaffService restaurantStaffService, RestaurantService restaurantService) {
        this.authenticatedUserService = authenticatedUserService;
        this.restaurantStaffService = restaurantStaffService;
        this.restaurantService = restaurantService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Restaurant>> create(@Valid @RequestBody RestaurantCreateRequest restaurantCreateRequest) {
        User creator = authenticatedUserService.getAuthenticatedUser();

        Restaurant restaurant = restaurantService.create(restaurantCreateRequest, creator);

        ApiResponse<Restaurant> response = ResponseUtil.success("Restaurant créé avec succès.", restaurant);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<RestaurantWithRoleDTO>>> index() {

        User user = authenticatedUserService.getAuthenticatedUser();
        List<RestaurantStaff> restaurants = restaurantStaffService.getRestaurantsForUser(user);

        List<RestaurantWithRoleDTO> restaurantWithRoleDTOS = restaurants.stream()
                .map(staff -> new RestaurantWithRoleDTO(
                        new RestaurantDTO(
                                staff.getRestaurant().getId(),
                                staff.getRestaurant().getName(),
                                staff.getRestaurant().getAddress(),
                                staff.getRestaurant().getCity(),
                                staff.getRestaurant().getZipcode(),
                                staff.getRestaurant().getCapacity(),
                                staff.getRestaurant().getOpenings()
                        ),
                        new RoleDTO(
                                staff.getRole().getId(),
                                staff.getRole().getRoleName()
                        )
                ))
                .toList();

        ApiResponse<List<RestaurantWithRoleDTO>> response = ResponseUtil.success("La liste des restaurants a été trouvée avec succès.", restaurantWithRoleDTOS);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
