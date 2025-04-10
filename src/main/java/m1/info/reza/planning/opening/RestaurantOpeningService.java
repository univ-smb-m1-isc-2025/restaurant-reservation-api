package m1.info.reza.planning.opening;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.planning.DTO.OpeningHoursRequest;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantRepository;
import m1.info.reza.restaurant.RestaurantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantOpeningService {

    private final RestaurantOpeningRepository openingRepository;
    private final RestaurantService restaurantService;

    public RestaurantOpeningService(RestaurantOpeningRepository openingRepository, RestaurantService restaurantService) {
        this.openingRepository = openingRepository;
        this.restaurantService = restaurantService;
    }

    @Transactional
    public Restaurant saveOpeningHours(Long restaurantId, List<OpeningHoursRequest> openings) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);

        for (OpeningHoursRequest request : openings) {
            if(request.getOpeningTime().isAfter(request.getClosingTime())){
                throw new IllegalArgumentException("Le créneau du " + request.getDay() + " de " + request.getOpeningTime() +
                        " à " + request.getClosingTime() + " a une heure d'ouverture qui est après la fermeture.");
            }

            if (isOverlapping(restaurant, request)) {
                throw new IllegalArgumentException("Le créneau du " + request.getDay() + " de " + request.getOpeningTime() +
                        " à " + request.getClosingTime() + " chevauche un créneau existant.");
            }

            RestaurantOpening newOpening = new RestaurantOpening(restaurant, request.getDay(), request.getOpeningTime(), request.getClosingTime());
            openingRepository.save(newOpening);
        }

        return restaurant;
    }

    public RestaurantOpening getOpening(Long openingId) {
        Optional<RestaurantOpening> opening = openingRepository.findById(openingId);

        if (opening.isPresent()) {
            return opening.get();
        }

        throw new EntityNotFoundException("Le créneau d'ouverture spécifié avec l'id" + openingId +" n'existe pas.");
    }

    public boolean isRestaurantOpenAt(LocalDateTime dateTime){
        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();
        LocalDate date = dateTime.toLocalDate();

        Optional<RestaurantOpening> openingOptional = openingRepository.findValidOpeningByDateTime(day, time, date);
        return openingOptional.isPresent();
    }

    private boolean isOverlapping(Restaurant restaurant, OpeningHoursRequest request){
        DayOfWeek day = request.getDay();
        LocalTime openingTime = request.getOpeningTime();
        LocalTime closingTime = request.getClosingTime();

       return openingRepository.existsByRestaurantAndDayAndTimeOverlap(
                restaurant, day, openingTime, closingTime);
    }
}
