package m1.info.reza.planning;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.planning.DTO.OpeningHoursRequest;
import m1.info.reza.planning.day.DayOfWeek;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class RestaurantOpeningService {

    private final RestaurantOpeningRepository openingRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantOpeningService(RestaurantOpeningRepository openingRepository, RestaurantRepository restaurantRepository) {
        this.openingRepository = openingRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Restaurant saveOpeningHours(Long restaurantId, List<OpeningHoursRequest> openings) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Le restaurant avec l'id" + restaurantId +" n'existe pas."));

        for (OpeningHoursRequest request : openings) {
            DayOfWeek day = request.getDay();
            LocalTime openingTime = request.getOpeningTime();
            LocalTime closingTime = request.getClosingTime();

            boolean isOverlapping = openingRepository.existsByRestaurantAndDayAndTimeOverlap(
                    restaurant, day, openingTime, closingTime);

            if (isOverlapping) {
                throw new IllegalArgumentException("Le créneau du " + day + " de " + openingTime + " à " + closingTime + " chevauche un créneau existant.");
            }

            RestaurantOpening newOpening = new RestaurantOpening(restaurant, day, openingTime, closingTime);
            openingRepository.save(newOpening);
        }

        return restaurant;
    }
}
