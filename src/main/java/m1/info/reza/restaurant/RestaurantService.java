package m1.info.reza.restaurant;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.restaurant.DTO.RestaurantCreateRequest;
import m1.info.reza.staff.RestaurantStaff;
import m1.info.reza.staff.RestaurantStaffService;
import m1.info.reza.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantStaffService restaurantStaffService;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantStaffService restaurantStaffService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantStaffService = restaurantStaffService;
    }

    public Restaurant create(RestaurantCreateRequest restaurantInformations, User creator){
        Restaurant restaurant = new Restaurant(
                restaurantInformations.getName(),
                restaurantInformations.getAddress(),
                restaurantInformations.getCity(),
                restaurantInformations.getZipcode(),
                restaurantInformations.getCapacity()
        );

        restaurantRepository.save(restaurant);
        restaurantStaffService.addOwner(restaurant, creator);

        return restaurant;
    }

    public Restaurant getRestaurant(Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);

        if (restaurant.isPresent()) {
            return restaurant.get();
        }

        throw new EntityNotFoundException("Le restaurant avec l'id" + restaurantId +" n'existe pas.");
    }
}
