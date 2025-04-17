package m1.info.reza.planning.closure;

import m1.info.reza.auth.AuthenticatedUserService;
import m1.info.reza.planning.closure.DTO.ClosureCreateRequest;
import m1.info.reza.planning.opening.RestaurantOpening;
import m1.info.reza.planning.opening.RestaurantOpeningService;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RestaurantClosureControllerTest {

    @Mock
    private RestaurantClosureService restaurantClosureService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @Mock
    private RestaurantOpeningService restaurantOpeningService;

    @InjectMocks
    private RestaurantClosureController restaurantClosureController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOpeningHours() {
        Long restaurantId = 1L;
        Long openingId = 2L;

        ClosureCreateRequest request = new ClosureCreateRequest();
        request.setOpeningId(openingId);
        request.setDateOfClosure(LocalDate.of(2025, 4, 30));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Resto Test");

        RestaurantOpening opening = new RestaurantOpening();
        opening.setId(openingId);

        RestaurantClosure closure = new RestaurantClosure();
        closure.setId(1L);
        closure.setClosureDate(request.getDateOfClosure());
        closure.setOpening(opening);

        when(restaurantService.getRestaurant(restaurantId)).thenReturn(restaurant);
        when(restaurantOpeningService.getOpening(openingId)).thenReturn(opening);
        when(restaurantClosureService.createRestaurantClosure(opening, request.getDateOfClosure())).thenReturn(closure);

        ResponseEntity<ApiResponse<RestaurantDTO>> response = restaurantClosureController.addOpeningHours(restaurantId, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getId()).isEqualTo(restaurantId);
        assertThat(response.getBody().getMessage()).contains("planning d'ouverture");

        verify(authenticatedUserService).checkAuthenticatedUserRoleManagerOrHigher(restaurantId);
        verify(restaurantClosureService).createRestaurantClosure(opening, request.getDateOfClosure());
    }
}
