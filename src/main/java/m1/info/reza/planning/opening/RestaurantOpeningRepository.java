package m1.info.reza.planning.opening;

import m1.info.reza.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


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

    @Query("""
    SELECT ro FROM RestaurantOpening ro
    WHERE ro.day = :day
      AND ro.openingTime <= :time
      AND ro.closingTime > :time
      AND NOT EXISTS (
          SELECT rc FROM RestaurantClosure rc
          WHERE rc.opening = ro AND rc.closureDate = :date
      )
    """)
    Optional<RestaurantOpening> findValidOpeningByDateTime(
            @Param("day") DayOfWeek day,
            @Param("time") LocalTime time,
            @Param("date") LocalDate date
    );


}