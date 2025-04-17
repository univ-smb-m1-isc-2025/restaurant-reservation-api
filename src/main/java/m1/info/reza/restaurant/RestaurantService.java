package m1.info.reza.restaurant;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.restaurant.DTO.RestaurantCreateRequest;
import m1.info.reza.staff.RestaurantStaff;
import m1.info.reza.staff.RestaurantStaffService;
import m1.info.reza.user.User;
import m1.info.reza.user_feedback.UserFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantStaffService restaurantStaffService;
    private final UserFeedbackService userFeedbackService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantStaffService restaurantStaffService, UserFeedbackService userFeedbackService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantStaffService = restaurantStaffService;
        this.userFeedbackService = userFeedbackService;
    }

    public Restaurant create(RestaurantCreateRequest restaurantInformations, User creator){
        Restaurant restaurant = new Restaurant(
                restaurantInformations.getName(),
                restaurantInformations.getAddress(),
                restaurantInformations.getCity(),
                restaurantInformations.getZipcode(),
                restaurantInformations.getCapacity()
        );

        Long subGroupId = 2L;
        try {
            String token = userFeedbackService.loginAndGetToken();
            subGroupId = userFeedbackService.createSubGroup(restaurant.getName(), token);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        restaurant.setSubGroupId(subGroupId);

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
