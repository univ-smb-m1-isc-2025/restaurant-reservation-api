package m1.info.reza.planning;

import jakarta.validation.Valid;
import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.planning.DTO.OpeningHoursRequest;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant/{id}/opening")
public class RestaurantOpeningController {

    private final RestaurantOpeningService openingService;
    private final AuthenticatedUserService authenticatedUserService;

    public RestaurantOpeningController(RestaurantOpeningService openingService, AuthenticatedUserService authenticatedUserService) {
        this.openingService = openingService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<RestaurantDTO>> addOpeningHours(@PathVariable Long id, @Valid @RequestBody List<OpeningHoursRequest> openings) {
        authenticatedUserService.checkAuthenticatedUserRoleManagerOrHigher(id);

        Restaurant restaurant = openingService.saveOpeningHours(id, openings);
        RestaurantDTO result = new RestaurantDTO(restaurant);

        ApiResponse<RestaurantDTO> response = ResponseUtil.success("Le planning d'ouverture du restaurant a été mis à jour.", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
