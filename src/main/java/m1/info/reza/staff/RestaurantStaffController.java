package m1.info.reza.staff;

import jakarta.validation.Valid;
import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantService;
import m1.info.reza.staff.DTO.CreateStaffRequest;
import m1.info.reza.staff.DTO.RestaurantStaffDTO;
import m1.info.reza.staff.DTO.UpdateStaffRequest;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.staff.roles.RoleService;
import m1.info.reza.user.User;
import m1.info.reza.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/restaurant/{restaurantId}/staff")
public class RestaurantStaffController {

    private final AuthenticatedUserService authenticatedUserService;
    private final RestaurantStaffService restaurantStaffService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final RoleService roleService;
    private final RestaurantStaffRepository restaurantStaffRepository;

    public RestaurantStaffController(AuthenticatedUserService authenticatedUserService, RestaurantStaffService restaurantStaffService, RestaurantService restaurantService, UserService userService, RoleService roleService, RestaurantStaffRepository restaurantStaffRepository) {
        this.authenticatedUserService = authenticatedUserService;
        this.restaurantStaffService = restaurantStaffService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.roleService = roleService;
        this.restaurantStaffRepository = restaurantStaffRepository;
    }

    @GetMapping("/all")
    private ResponseEntity<ApiResponse<List<RestaurantStaffDTO>>> index(@PathVariable Long restaurantId){
        authenticatedUserService.checkAuthenticatedUserIsStaff(restaurantId);

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        List<RestaurantStaff> staff = restaurantStaffService.getAllStaffFromRestaurant(restaurant);

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

        restaurantStaffService.create(restaurant, user, role);
        List<RestaurantStaff> staff = restaurantStaffService.getAllStaffFromRestaurant(restaurant);

        List<RestaurantStaffDTO> staffDTOs = staff.stream()
                .map(staffMember ->
                        new RestaurantStaffDTO(staffMember.getUser(), staffMember.getRole())
                )
                .toList();

        ApiResponse<List<RestaurantStaffDTO>> response = ResponseUtil.success("L'employé a été ajouté avec succès.", staffDTOs);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    private ResponseEntity<ApiResponse<RestaurantStaffDTO>> update(
            @PathVariable Long restaurantId,
            @Valid @RequestBody UpdateStaffRequest request
    ){
        authenticatedUserService.checkAuthenticatedUserRoleManagerOrHigher(restaurantId);

        RestaurantStaff staff = restaurantStaffService.getRestaurantStaff(restaurantId, request.getUserId());
        if(!Objects.equals(staff.getRestaurant().getId(), restaurantId)){
            throw new BadRequestException("Vous ne pouvez pas modifier un employé d'un restaurant autre que celui spécifié dans la requête");
        }

        if(Objects.equals(staff.getRole().getId(), roleService.getOwnerRole().getId())){
            throw new BadRequestException("Vous ne pouvez pas modifier le rôle de l'OWNER.");
        }

        Role role = roleService.getRole(request.getRoleId());
        if(Objects.equals(staff.getRole().getId(), request.getRoleId())){
            throw new BadRequestException("Cet employé possède déjà ce role.");
        }

        staff = restaurantStaffService.update(staff, role);
        RestaurantStaffDTO staffDTO = new RestaurantStaffDTO(staff.getUser(), staff.getRole());

        ApiResponse<RestaurantStaffDTO> response = ResponseUtil.success("L'employé a été mis à jour avec succès.", staffDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
