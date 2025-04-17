package m1.info.reza.reservation;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.customer.Customer;
import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.planning.opening.RestaurantOpening;
import m1.info.reza.planning.opening.RestaurantOpeningRepository;
import m1.info.reza.planning.opening.RestaurantOpeningService;
import m1.info.reza.reservation.status.ReservationStatus;
import m1.info.reza.restaurant.Restaurant;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

        if(!openingService.isRestaurantOpenAt(restaurant, reservationDate)){
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


    public Reservation confirm(Reservation reservation) {
        if(reservation.getReservationStatus() != ReservationStatus.PENDING){
            throw new BadRequestException("Vous ne pouvez pas valider une réservation qui n'est pas en attente.");
        }

        reservation.setReservationStatus(ReservationStatus.COMPLETED);
        return reservationRepository.save(reservation);
    }

    public Reservation cancel(Reservation reservation) {
        if(reservation.getReservationStatus() == ReservationStatus.COMPLETED){
            throw new BadRequestException("Vous ne pouvez pas annuler une réservation qui a été confirmée.");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }


    public Reservation getReservation(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if(reservation.isEmpty()){
            throw new EntityNotFoundException("La réservation avec l'id : " + id + " n'existe pas");
        }

        return reservation.get();
    }

    public List<Reservation> findPendingReservationsForTomorrow(){
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        LocalDateTime startOfDay = tomorrow.atStartOfDay();
        LocalDateTime endOfDay = tomorrow.atTime(LocalTime.MAX);

        return reservationRepository.findByReservationDateBetweenAndReservationStatus(startOfDay, endOfDay, ReservationStatus.PENDING);
    }

    public List<Reservation> findCompletedReservationsFromYesterday(){
        LocalDate yesterday = LocalDate.now().minusDays(1);

        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        return reservationRepository.findByReservationDateBetweenAndReservationStatus(startOfDay, endOfDay, ReservationStatus.COMPLETED);
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
