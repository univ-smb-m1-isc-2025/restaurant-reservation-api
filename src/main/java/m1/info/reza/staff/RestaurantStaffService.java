package m1.info.reza.staff;

import jakarta.transaction.Transactional;
import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantRepository;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.staff.roles.RoleService;
import m1.info.reza.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RestaurantStaffService {

    private final RestaurantStaffRepository restaurantStaffRepository;
    private final RoleService roleService;
    private final RestaurantRepository restaurantRepository;

    public RestaurantStaffService(RestaurantStaffRepository restaurantStaffRepository, RoleService roleService, RestaurantRepository restaurantRepository) {
        this.restaurantStaffRepository = restaurantStaffRepository;
        this.roleService = roleService;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public void addOwner(Restaurant restaurant, User user) {
        Role ownerRole = roleService.getOwnerRole();

        RestaurantStaff restaurantStaff = new RestaurantStaff(user, restaurant, ownerRole);
        restaurantStaffRepository.save(restaurantStaff);
    }

    public RestaurantStaff addStaff(Restaurant restaurant, User user, Role role) {
        if(Objects.equals(role.getRoleName(), "OWNER")){
            throw new BadRequestException("Vous ne pouvez pas ajouter un utilisateur au rôle d'OWNER.");
        }

        if(restaurantStaffRepository.findByUserIdAndRestaurantId(user.getId(), restaurant.getId()).isPresent()){
            throw new BadRequestException("Vous ne pouvez pas ajouter un utilisateur au staff si il en fait déjà partie.");
        }

        RestaurantStaff restaurantStaff = new RestaurantStaff(user, restaurant, role);
        restaurantStaffRepository.save(restaurantStaff);

        return restaurantStaff;
    }

    public List<RestaurantStaff> getRestaurantsForUser(User user) {
        List<RestaurantStaff> staffList = restaurantStaffRepository.findByUser(user);

        return staffList;
    }

    public List<RestaurantStaff> getRestaurantStaff(Restaurant restaurant){
        List<RestaurantStaff> staffList = restaurantStaffRepository.findByRestaurant(restaurant);

        return staffList;
    }

}
