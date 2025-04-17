package m1.info.reza.reservation;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.customer.Customer;
import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.planning.opening.RestaurantOpening;
import m1.info.reza.planning.opening.RestaurantOpeningService;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.reservation.status.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantOpeningService openingService;

    @InjectMocks
    private ReservationService reservationService;

    private Restaurant restaurant;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurant = new Restaurant(); // Stub
        restaurant.setCapacity(50); // Example capacity
        customer = new Customer(); // Stub
    }

    @Test
    void create_shouldThrowException_whenGuestsExceedCapacity() {
        LocalDateTime reservationDate = LocalDateTime.of(2025, 4, 17, 19, 0);
        int nbGuests = 60; // Exceeds capacity

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationService.create(restaurant, customer, reservationDate, nbGuests)
        );

        assertEquals("Le nombre d'invités excède la capacté totale du restaurant.", ex.getMessage());
    }

    @Test
    void create_shouldThrowException_whenRestaurantClosed() {
        LocalDateTime reservationDate = LocalDateTime.of(2025, 4, 17, 19, 0);
        int nbGuests = 2;

        when(openingService.isRestaurantOpenAt(restaurant, reservationDate)).thenReturn(false);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationService.create(restaurant, customer, reservationDate, nbGuests)
        );

        assertEquals("Le restaurant n'est pas ouvert à cet horaire.", ex.getMessage());
    }

    @Test
    void create_shouldThrowException_whenRestaurantFull() {
        LocalDateTime reservationDate = LocalDateTime.of(2025, 4, 17, 19, 0);
        int nbGuests = 2;
        int guestCount = 50; // Full capacity

        when(openingService.isRestaurantOpenAt(restaurant, reservationDate)).thenReturn(true);
        when(reservationRepository.countGuestsAroundTime(restaurant, reservationDate.minusMinutes(90), reservationDate.plusMinutes(90)))
                .thenReturn(guestCount);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationService.create(restaurant, customer, reservationDate, nbGuests)
        );

        assertEquals("Le restaurant est complet à cet horaire.", ex.getMessage());
    }

    @Test
    void create_shouldThrowException_whenNotEnoughCapacityAtTime() {
        LocalDateTime reservationDate = LocalDateTime.of(2025, 4, 17, 19, 0);
        int nbGuests = 5;
        int guestCount = 46; // 46 guests already reserved

        when(openingService.isRestaurantOpenAt(restaurant, reservationDate)).thenReturn(true);
        when(reservationRepository.countGuestsAroundTime(restaurant, reservationDate.minusMinutes(90), reservationDate.plusMinutes(90)))
                .thenReturn(guestCount);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationService.create(restaurant, customer, reservationDate, nbGuests)
        );

        assertEquals("Le restaurant ne peut accepter qu'un maximum de 4 personnes à cet horaire.", ex.getMessage());
    }

    @Test
    void create_shouldSaveReservation_whenValid() {
        LocalDateTime reservationDate = LocalDateTime.of(2025, 4, 17, 19, 0);
        int nbGuests = 2;

        when(openingService.isRestaurantOpenAt(restaurant, reservationDate)).thenReturn(true);
        when(reservationRepository.countGuestsAroundTime(restaurant, reservationDate.minusMinutes(90), reservationDate.plusMinutes(90)))
                .thenReturn(0); // No guests yet

        Reservation reservation = reservationService.create(restaurant, customer, reservationDate, nbGuests);

        assertNotNull(reservation);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void getReservationsForOpeningOnDate_shouldThrowException_whenDateDoesNotMatchOpeningDay() {
        LocalDate date = LocalDate.of(2025, 4, 17);
        RestaurantOpening opening = new RestaurantOpening();
        opening.setDay(DayOfWeek.MONDAY);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationService.getReservationsForOpeningOnDate(restaurant, opening, date)
        );

        assertEquals("La date ne correspond pas au jour de l'ouverture.", ex.getMessage());
    }

    @Test
    void getReservationsForOpeningOnDate_shouldReturnReservations_whenValid() {
        LocalDate date = LocalDate.of(2025, 4, 17);
        RestaurantOpening opening = new RestaurantOpening();
        opening.setDay(DayOfWeek.THURSDAY);
        opening.setOpeningTime(LocalTime.of(12, 0));
        opening.setClosingTime(LocalTime.of(22, 0));

        Restaurant restaurant = mock(Restaurant.class);
        when(restaurant.getId()).thenReturn(1L); // Mock restaurant ID

        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findReservationsBetweenDates(1L,
                date.atTime(opening.getOpeningTime()),
                date.atTime(opening.getClosingTime())))
                .thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationsForOpeningOnDate(restaurant, opening, date);

        assertEquals(2, result.size());
    }


    @Test
    void confirm_shouldThrowException_whenReservationNotPending() {
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.COMPLETED);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationService.confirm(reservation)
        );

        assertEquals("Vous ne pouvez pas valider une réservation qui n'est pas en attente.", ex.getMessage());
    }

    @Test
    void confirm_shouldUpdateStatusToCompleted() {
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.PENDING);

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.confirm(reservation);

        assertEquals(ReservationStatus.COMPLETED, result.getReservationStatus());
    }

    @Test
    void cancel_shouldThrowException_whenReservationCompleted() {
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.COMPLETED);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationService.cancel(reservation)
        );

        assertEquals("Vous ne pouvez pas annuler une réservation qui a été confirmée.", ex.getMessage());
    }

    @Test
    void cancel_shouldUpdateStatusToCanceled() {
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.PENDING);

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.cancel(reservation);

        assertEquals(ReservationStatus.CANCELED, result.getReservationStatus());
    }

    @Test
    void getReservation_shouldThrowException_whenNotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                reservationService.getReservation(99L)
        );

        assertEquals("La réservation avec l'id : 99 n'existe pas", ex.getMessage());
    }

    @Test
    void getReservation_shouldReturnReservation_whenFound() {
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservation(1L);

        assertEquals(reservation, result);
    }
}
