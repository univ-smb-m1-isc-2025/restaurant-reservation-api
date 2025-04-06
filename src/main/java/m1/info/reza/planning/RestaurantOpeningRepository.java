package m1.info.reza.planning;

import m1.info.reza.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;


public interface RestaurantOpeningRepository extends JpaRepository<RestaurantOpening, Long> {

    @Query("SELECT COUNT(ro) > 0 FROM RestaurantOpening ro WHERE ro.restaurant = :restaurant " +
            "AND ro.day = :day " +
            "AND ((:openingTime BETWEEN ro.openingTime AND ro.closingTime) " +
            "OR (:closingTime BETWEEN ro.openingTime AND ro.closingTime) " +
            "OR (ro.openingTime BETWEEN :openingTime AND :closingTime))")
    boolean existsByRestaurantAndDayAndTimeOverlap(@Param("restaurant") Restaurant restaurant,
                                                   @Param("day") DayOfWeek day,
                                                   @Param("openingTime") LocalTime openingTime,
                                                   @Param("closingTime") LocalTime closingTime);
}