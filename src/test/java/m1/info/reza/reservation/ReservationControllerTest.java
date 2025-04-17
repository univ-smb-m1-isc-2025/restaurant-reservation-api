package m1.info.reza.reservation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import m1.info.reza.planning.opening.RestaurantOpening;
import m1.info.reza.reservation.Reservation;
import m1.info.reza.reservation.ReservationService;
import m1.info.reza.customer.CustomerService;
import m1.info.reza.restaurant.RestaurantService;
import m1.info.reza.mail.MailService;
import m1.info.reza.planning.opening.RestaurantOpeningService;
import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.reservation.DTO.CreateReservationRequest;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private CustomerService customerService;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private RestaurantOpeningService openingService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    // Test GET /opening/{openingId}/date/{date}
    @Test
    void getReservationsForOpeningOnDate_shouldReturnReservations_whenValidRequest() throws Exception {
        Long restaurantId = 1L;
        Long openingId = 1L;
        LocalDate date = LocalDate.now();

        Restaurant restaurant = new Restaurant(); // Simulate Restaurant object
        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);
        when(openingService.getOpening(openingId)).thenReturn(new RestaurantOpening()); // Simulate RestaurantOpening

        mockMvc.perform(get("/reservation/{restaurantId}/opening/{openingId}/date/{date}", restaurantId, openingId, date)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("La liste des réservations pour la date donnée a été récupérée avec succès."));
    }

    // Test POST /confirm/{reservationId}
    @Test
    void validate_shouldReturnOk_whenReservationConfirmed() throws Exception {
        Long restaurantId = 1L;
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        when(reservationService.getReservation(reservationId)).thenReturn(reservation);
        when(reservationService.confirm(reservation)).thenReturn(reservation);

        mockMvc.perform(post("/reservation/{restaurantId}/confirm/{reservationId}", restaurantId, reservationId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("La réservation a été confirmée avec succès."));
    }

    // Test DELETE /cancel/{reservationId}
    @Test
    void cancel_shouldReturnOk_whenReservationCancelled() throws Exception {
        Long restaurantId = 1L;
        Long reservationId = 1L;

        Reservation reservation = new Reservation();
        when(reservationService.getReservation(reservationId)).thenReturn(reservation);
        when(reservationService.cancel(reservation)).thenReturn(reservation);

        mockMvc.perform(delete("/reservation/{restaurantId}/cancel/{reservationId}", restaurantId, reservationId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("La réservation a été annulée avec succès."));
    }
}

