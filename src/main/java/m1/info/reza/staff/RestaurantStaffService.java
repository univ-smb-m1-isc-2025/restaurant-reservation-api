package m1.info.reza.staff;

import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

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

    public RestaurantStaff create(Restaurant restaurant, User user, Role role) {
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

    public RestaurantStaff update(RestaurantStaff staff, Role role) {
        if(Objects.equals(role.getRoleName(), "OWNER")){
            throw new BadRequestException("Vous ne pouvez définir un utilisateur au rôle d'OWNER.");
        }

        staff.setRole(role);
        return restaurantStaffRepository.save(staff);
    }


    public List<RestaurantStaff> getRestaurantsForUser(User user) {
        List<RestaurantStaff> staffList = restaurantStaffRepository.findByUser(user);

        return staffList;
    }

    public List<RestaurantStaff> getAllStaffFromRestaurant(Restaurant restaurant){
        List<RestaurantStaff> staffList = restaurantStaffRepository.findByRestaurant(restaurant);

        return staffList;
    }

    public RestaurantStaff getRestaurantStaff(Long restaurantId, Long restaurantStaffId){
        Optional<RestaurantStaff> staffOptional = restaurantStaffRepository.findByUserIdAndRestaurantId(restaurantStaffId, restaurantId);

        if (staffOptional.isPresent()) {
            return staffOptional.get();
        }

        throw new EntityNotFoundException("Le membre du staff avec l'id " + restaurantStaffId +" n'existe pas.");
    }

}
