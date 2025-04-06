package m1.info.reza.planning.closure;

import m1.info.reza.planning.RestaurantOpening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RestaurantClosureRepository extends JpaRepository<RestaurantClosure, Long> {
    RestaurantClosure findByOpeningAndClosureDate(RestaurantOpening opening, LocalDate closureDate);
}
