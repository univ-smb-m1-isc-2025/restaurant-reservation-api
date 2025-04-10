package m1.info.reza.reservation;

import m1.info.reza.reservation.status.ReservationStatus;
import m1.info.reza.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
    SELECT SUM(r.nbGuests) FROM Reservation r
    WHERE r.restaurant = :restaurant
      AND r.reservationDate BETWEEN :start AND :end
    """)
    Integer countGuestsAroundTime(
            @Param("restaurant") Restaurant restaurant,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
