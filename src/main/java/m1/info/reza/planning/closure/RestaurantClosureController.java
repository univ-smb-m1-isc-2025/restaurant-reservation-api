package m1.info.reza.planning.closure;

import jakarta.validation.Valid;
import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.planning.opening.RestaurantOpening;
import m1.info.reza.planning.opening.RestaurantOpeningService;
import m1.info.reza.planning.closure.DTO.ClosureCreateRequest;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant/{restaurantId}/closure")
public class RestaurantClosureController {

    private final RestaurantClosureService restaurantClosureService;
    private final RestaurantService restaurantService;
    private final AuthenticatedUserService authenticatedUserService;
    private final RestaurantOpeningService restaurantOpeningService;

    public RestaurantClosureController(RestaurantClosureService restaurantClosureService, RestaurantService restaurantService, AuthenticatedUserService authenticatedUserService, RestaurantOpeningService restaurantOpeningService) {
        this.restaurantClosureService = restaurantClosureService;
        this.restaurantService = restaurantService;
        this.authenticatedUserService = authenticatedUserService;
        this.restaurantOpeningService = restaurantOpeningService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RestaurantDTO>> addOpeningHours(@PathVariable Long restaurantId, @Valid  @RequestBody ClosureCreateRequest closureCreateRequest) {
        authenticatedUserService.checkAuthenticatedUserRoleManagerOrHigher(restaurantId);

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        RestaurantOpening opening = restaurantOpeningService.getOpening(closureCreateRequest.getOpeningId());

        RestaurantClosure closure = restaurantClosureService.createRestaurantClosure(opening, closureCreateRequest.getDateOfClosure());
        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurant);

        ApiResponse<RestaurantDTO> response = ResponseUtil.success("Le planning d'ouverture du restaurant a été mis à jour.", restaurantDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
