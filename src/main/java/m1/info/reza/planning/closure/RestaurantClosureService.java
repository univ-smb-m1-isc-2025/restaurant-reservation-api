package m1.info.reza.planning.closure;

import jakarta.persistence.EntityExistsException;
import m1.info.reza.exception.custom.BadRequestException;

import m1.info.reza.planning.RestaurantOpening;
import m1.info.reza.planning.closure.DTO.ClosureCreateRequest;
import m1.info.reza.restaurant.Restaurant;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RestaurantClosureService {

    private final RestaurantClosureRepository restaurantClosureRepository;

    public RestaurantClosureService(RestaurantClosureRepository restaurantClosureRepository) {
        this.restaurantClosureRepository = restaurantClosureRepository;
    }

    public RestaurantClosure createRestaurantClosure(RestaurantOpening opening, LocalDate closureDate) {
        if(closureDate.getDayOfWeek() != opening.getDay()){
            throw new BadRequestException("La date spécifiée ne correspond pas au jour du créneau spécifié. (" + closureDate.getDayOfWeek() + " != " + opening.getDay() + ")");
        }

        RestaurantClosure existingClosure = restaurantClosureRepository
                .findByOpeningAndClosureDate(opening, closureDate);

        if (existingClosure != null) {
            throw new EntityExistsException("Ce créneau est déjà annulé pour la date spécifiée.");
        }

        RestaurantClosure newClosure = new RestaurantClosure();
        newClosure.setOpening(opening);
        newClosure.setClosureDate(closureDate);

        return restaurantClosureRepository.save(newClosure);
    }

}
