package m1.info.reza.auth;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.exception.custom.UnauthorizedAccessException;
import m1.info.reza.staff.RestaurantStaff;
import m1.info.reza.staff.RestaurantStaffRepository;
import m1.info.reza.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticatedUserService {

    private final RestaurantStaffRepository restaurantStaffRepository;

    public AuthenticatedUserService(RestaurantStaffRepository restaurantStaffRepository) {
        this.restaurantStaffRepository = restaurantStaffRepository;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null; // Aucun utilisateur connecté
    }

    public void checkAuthenticatedUserRoleManagerOrHiger(Long restaurantId){
        User user = this.getAuthenticatedUser();
        Optional<RestaurantStaff> restaurantStaff = restaurantStaffRepository.findByUserAndRestaurantId(user, restaurantId);

        if(restaurantStaff.isPresent()){
            RestaurantStaff staff = restaurantStaff.get();
            if (staff.getRole().getRoleName().equals("OWNER") || staff.getRole().getRoleName().equals("MANAGER")){
                return;
            }
        }

        throw new UnauthorizedAccessException("Vous n'avez pas les permissions nécessaires afin de faire cette opération.");
    }
}