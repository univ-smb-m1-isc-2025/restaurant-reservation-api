package m1.info.reza.staff;

import jakarta.transaction.Transactional;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.staff.roles.RoleService;
import m1.info.reza.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantStaffService {

    private final RestaurantStaffRepository restaurantStaffRepository;
    private final RoleService roleService;

    public RestaurantStaffService(RestaurantStaffRepository restaurantStaffRepository, RoleService roleService) {
        this.restaurantStaffRepository = restaurantStaffRepository;
        this.roleService = roleService;
    }

    @Transactional
    public void addOwner(Restaurant restaurant, User user) {
        Role ownerRole = roleService.getOwnerRole();

        RestaurantStaff restaurantStaff = new RestaurantStaff(user, restaurant, ownerRole);
        restaurantStaffRepository.save(restaurantStaff);
    }

    public List<RestaurantStaff> getRestaurantsForUser(User user) {
        List<RestaurantStaff> staffList = restaurantStaffRepository.findByUser(user);

        return staffList;
    }

}
