package m1.info.reza.reservation;

import jakarta.validation.Valid;
import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.customer.Customer;
import m1.info.reza.customer.CustomerService;
import m1.info.reza.planning.opening.RestaurantOpening;
import m1.info.reza.planning.opening.RestaurantOpeningService;
import m1.info.reza.reservation.DTO.CreateReservationRequest;
import m1.info.reza.reservation.DTO.ReservationDTO;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantService;
import m1.info.reza.staff.DTO.RestaurantStaffDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation/{restaurantId}")
public class ReservationController {

    private final RestaurantService restaurantService;
    private final ReservationService reservationService;
    private final CustomerService customerService;
    private final AuthenticatedUserService  authenticatedUserService;
    private final RestaurantOpeningService openingService;
    private final AuthenticatedUserService authenticatedUserService;

    public ReservationController(RestaurantService restaurantService, ReservationService reservationService, CustomerService customerService, AuthenticatedUserService authenticatedUserService) {
    public ReservationController(RestaurantService restaurantService, ReservationService reservationService, CustomerService customerService, RestaurantOpeningService openingService, AuthenticatedUserService authenticatedUserService) {
        this.restaurantService = restaurantService;
        this.reservationService = reservationService;
        this.customerService = customerService;
        this.authenticatedUserService = authenticatedUserService;
        this.openingService = openingService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Reservation>> create(@PathVariable Long restaurantId, @Valid @RequestBody CreateReservationRequest request){
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        Customer customer = customerService.findOrCreate(request.getCustomerPhone());

        Reservation reservation = reservationService.create(restaurant, customer, request.getReservationDate(), request.getNbGuests());

        ApiResponse<Reservation> response = ResponseUtil.success("La réservation a été prise en compte.", reservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/opening/{openingId}/date/{date}")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getReservationsForOpeningOnDate(
            @PathVariable Long restaurantId,
            @PathVariable Long openingId,
            @PathVariable LocalDate date
    ) {
        authenticatedUserService.checkAuthenticatedUserIsStaff(restaurantId);

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        RestaurantOpening opening = openingService.getOpening(openingId);

        List<Reservation> reservations = reservationService.getReservationsForOpeningOnDate(restaurant, opening, date);

        List<ReservationDTO> reservationDTOS = reservations.stream()
                .map(ReservationDTO::new)
                .toList();

        ApiResponse<List<ReservationDTO>> response = ResponseUtil.success("La liste des réservations pour la date donnée a été récupérée avec succès.", reservationDTOS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/confirm/{reservationId}")
    public ResponseEntity<ApiResponse<Reservation>> validate(@PathVariable Long restaurantId, @PathVariable Long reservationId){
        authenticatedUserService.checkAuthenticatedUserIsStaff(restaurantId);

        Reservation reservation = reservationService.getReservation(reservationId);
        reservation = reservationService.confirm(reservation);

        ApiResponse<Reservation> response = ResponseUtil.success("La réservation a été confirmée avec succès.", reservation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<ApiResponse<Reservation>> cancel(@PathVariable Long restaurantId, @PathVariable Long reservationId){
        Reservation reservation = reservationService.getReservation(reservationId);

        reservation = reservationService.cancel(reservation);

        //TODO : Envoyer mail

        ApiResponse<Reservation> response = ResponseUtil.success("La réservation a été annulée avec succès.", reservation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
