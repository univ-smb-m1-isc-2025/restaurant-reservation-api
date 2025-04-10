package m1.info.reza.reservation;

import jakarta.validation.Valid;
import m1.info.reza.customer.Customer;
import m1.info.reza.customer.CustomerService;
import m1.info.reza.reservation.DTO.CreateReservationRequest;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantService;
import m1.info.reza.staff.DTO.RestaurantStaffDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation/{restaurantId}")
public class ReservationController {

    private final RestaurantService restaurantService;
    private final ReservationService reservationService;
    private final CustomerService customerService;

    public ReservationController(RestaurantService restaurantService, ReservationService reservationService, CustomerService customerService) {
        this.restaurantService = restaurantService;
        this.reservationService = reservationService;
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Reservation>> create(@PathVariable Long restaurantId, @Valid @RequestBody CreateReservationRequest request){
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        Customer customer = customerService.findOrCreate(request.getCustomerPhone());

        Reservation reservation = reservationService.create(restaurant, customer, request.getReservationDate(), request.getNbGuests());

        ApiResponse<Reservation> response = ResponseUtil.success("La réservation a été prise en compte.", reservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
