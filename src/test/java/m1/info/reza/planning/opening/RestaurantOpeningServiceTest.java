package m1.info.reza.planning.opening;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.planning.DTO.OpeningHoursRequest;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.restaurant.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantOpeningServiceTest {

    @Mock
    private RestaurantOpeningRepository openingRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantOpeningService restaurantOpeningService;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurant = new Restaurant(); // Stub
    }

    @Test
    void saveOpeningHours_shouldThrowException_whenOpeningTimeAfterClosingTime() {
        OpeningHoursRequest badRequest = new OpeningHoursRequest(DayOfWeek.MONDAY,
                LocalTime.of(18, 0), LocalTime.of(12, 0)); // Invalid: opening > closing

        when(restaurantService.getRestaurant(1L)).thenReturn(restaurant);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                restaurantOpeningService.saveOpeningHours(1L, List.of(badRequest))
        );

        assertTrue(ex.getMessage().contains("a une heure d'ouverture qui est après la fermeture."));
    }

    @Test
    void saveOpeningHours_shouldThrowException_whenOverlapping() {
        OpeningHoursRequest overlappingRequest = new OpeningHoursRequest(DayOfWeek.TUESDAY,
                LocalTime.of(12, 0), LocalTime.of(15, 0));

        when(restaurantService.getRestaurant(1L)).thenReturn(restaurant);
        when(openingRepository.existsByRestaurantAndDayAndTimeOverlap(
                eq(restaurant), eq(DayOfWeek.TUESDAY),
                any(LocalTime.class), any(LocalTime.class))
        ).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                restaurantOpeningService.saveOpeningHours(1L, List.of(overlappingRequest))
        );

        assertTrue(ex.getMessage().contains("chevauche un créneau existant."));
    }

    @Test
    void saveOpeningHours_shouldSave_whenValid() {
        OpeningHoursRequest validRequest = new OpeningHoursRequest(DayOfWeek.WEDNESDAY,
                LocalTime.of(10, 0), LocalTime.of(14, 0));

        when(restaurantService.getRestaurant(1L)).thenReturn(restaurant);
        when(openingRepository.existsByRestaurantAndDayAndTimeOverlap(
                any(), any(), any(), any())
        ).thenReturn(false);

        Restaurant result = restaurantOpeningService.saveOpeningHours(1L, List.of(validRequest));

        assertEquals(restaurant, result);
        verify(openingRepository, times(1)).save(any(RestaurantOpening.class));
    }

    @Test
    void getOpening_shouldReturnOpening_whenExists() {
        RestaurantOpening opening = new RestaurantOpening();
        when(openingRepository.findById(42L)).thenReturn(Optional.of(opening));

        RestaurantOpening result = restaurantOpeningService.getOpening(42L);

        assertEquals(opening, result);
    }

    @Test
    void getOpening_shouldThrow_whenNotFound() {
        when(openingRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                restaurantOpeningService.getOpening(99L)
        );

        assertTrue(ex.getMessage().contains("n'existe pas"));
    }

    @Test
    void isRestaurantOpenAt_shouldReturnTrue_whenValidOpeningExists() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 4, 17, 12, 0); // Thursday
        RestaurantOpening opening = new RestaurantOpening();

        when(openingRepository.findValidOpeningByDateTime(
                eq(restaurant),
                eq(DayOfWeek.THURSDAY),
                eq(LocalTime.of(12, 0)),
                eq(LocalDate.of(2025, 4, 17))
        )).thenReturn(Optional.of(opening));

        boolean result = restaurantOpeningService.isRestaurantOpenAt(restaurant, dateTime);

        assertTrue(result);
    }

    @Test
    void isRestaurantOpenAt_shouldReturnFalse_whenNoOpening() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 4, 17, 22, 0); // Thursday

        when(openingRepository.findValidOpeningByDateTime(
                any(), any(), any(), any())
        ).thenReturn(Optional.empty());

        boolean result = restaurantOpeningService.isRestaurantOpenAt(restaurant, dateTime);

        assertFalse(result);
    }
}
