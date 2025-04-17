package m1.info.reza.planning.closure;

import m1.info.reza.exception.custom.BadRequestException;
import m1.info.reza.planning.opening.RestaurantOpening;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import jakarta.persistence.EntityExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantClosureServiceTest {

    @Mock
    private RestaurantClosureRepository restaurantClosureRepository;

    @InjectMocks
    private RestaurantClosureService restaurantClosureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRestaurantClosure_shouldThrowBadRequest_whenDateDoesNotMatchOpeningDay() {
        RestaurantOpening opening = new RestaurantOpening();
        opening.setDay(DayOfWeek.MONDAY);

        LocalDate closureDate = LocalDate.of(2025, 4, 17); // Thursday

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> restaurantClosureService.createRestaurantClosure(opening, closureDate)
        );

        assertTrue(exception.getMessage().contains("La date spécifiée ne correspond pas au jour du créneau spécifié"));
        verify(restaurantClosureRepository, never()).save(any());
    }

    @Test
    void createRestaurantClosure_shouldThrowEntityExists_whenClosureAlreadyExists() {
        RestaurantOpening opening = new RestaurantOpening();
        opening.setDay(DayOfWeek.THURSDAY);

        LocalDate closureDate = LocalDate.of(2025, 4, 17); // Thursday

        when(restaurantClosureRepository.findByOpeningAndClosureDate(opening, closureDate))
                .thenReturn(new RestaurantClosure());

        assertThrows(EntityExistsException.class, () ->
                restaurantClosureService.createRestaurantClosure(opening, closureDate)
        );

        verify(restaurantClosureRepository, never()).save(any());
    }

    @Test
    void createRestaurantClosure_shouldSaveAndReturnClosure_whenValid() {
        RestaurantOpening opening = new RestaurantOpening();
        opening.setDay(DayOfWeek.THURSDAY);

        LocalDate closureDate = LocalDate.of(2025, 4, 17); // Thursday

        when(restaurantClosureRepository.findByOpeningAndClosureDate(opening, closureDate)).thenReturn(null);

        RestaurantClosure expectedClosure = new RestaurantClosure();
        expectedClosure.setOpening(opening);
        expectedClosure.setClosureDate(closureDate);

        when(restaurantClosureRepository.save(any(RestaurantClosure.class))).thenReturn(expectedClosure);

        RestaurantClosure result = restaurantClosureService.createRestaurantClosure(opening, closureDate);

        assertEquals(opening, result.getOpening());
        assertEquals(closureDate, result.getClosureDate());
        verify(restaurantClosureRepository).save(any(RestaurantClosure.class));
    }
}
