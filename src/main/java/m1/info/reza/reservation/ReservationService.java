package m1.info.reza.reservation;

import m1.info.reza.customer.Customer;
import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.planning.opening.RestaurantOpening;
import m1.info.reza.planning.opening.RestaurantOpeningRepository;
import m1.info.reza.planning.opening.RestaurantOpeningService;
import m1.info.reza.restaurant.Restaurant;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantOpeningService openingService;

    public ReservationService(ReservationRepository reservationRepository, RestaurantOpeningService openingService) {
        this.reservationRepository = reservationRepository;
        this.openingService = openingService;
    }

    public Reservation create(Restaurant restaurant, Customer customer, LocalDateTime reservationDate, int nbGuests) {
        if(nbGuests > restaurant.getCapacity()){
            throw new BadRequestException("Le nombre d'invités excède la capacté totale du restaurant.");
        }

        if(!openingService.isRestaurantOpenAt(reservationDate)){
            throw new BadRequestException("Le restaurant n'est pas ouvert à cet horaire.");
        }

        int guestCount = getGuestCountAroundTime(restaurant, reservationDate);
        if(guestCount == restaurant.getCapacity()){
            throw new BadRequestException("Le restaurant est complet à cet horaire.");
        }
        else if(guestCount + nbGuests > restaurant.getCapacity()){
            int maxGuests = restaurant.getCapacity() - guestCount;
            throw new BadRequestException("Le restaurant ne peut accepter qu'un maximum de "+ maxGuests +" personnes à cet horaire.");
        }

        Reservation reservation = new Reservation(restaurant, customer, reservationDate, nbGuests);
        reservationRepository.save(reservation);
        return reservation;
    }

    public List<Reservation> getReservationsForOpeningOnDate(Restaurant restaurant, RestaurantOpening opening, LocalDate date) {
        if (!opening.getDay().equals(date.getDayOfWeek())) {
            throw new BadRequestException("La date ne correspond pas au jour de l'ouverture.");
        }

        LocalDateTime startDateTime = date.atTime(opening.getOpeningTime());
        LocalDateTime endDateTime = date.atTime(opening.getClosingTime());

        return reservationRepository.findReservationsBetweenDates(restaurant.getId(), startDateTime, endDateTime);
    }



    private int getGuestCountAroundTime(Restaurant restaurant, LocalDateTime targetTime) {
        LocalDateTime start = targetTime.minusMinutes(90);
        LocalDateTime end = targetTime.plusMinutes(90);
        Integer count = reservationRepository.countGuestsAroundTime(
                restaurant,
                start,
                end
        );
        return count != null ? count : 0;
    }


}
