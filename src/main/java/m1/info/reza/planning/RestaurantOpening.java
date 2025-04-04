package m1.info.reza.planning;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import m1.info.reza.planning.day.DayOfWeek;
import m1.info.reza.restaurant.Restaurant;

import java.time.LocalTime;

@Entity
public class RestaurantOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    private LocalTime openingTime;
    private LocalTime closingTime;


    public RestaurantOpening() {
    }

    public RestaurantOpening(Restaurant restaurant, DayOfWeek day, LocalTime openingTime, LocalTime closingTime) {
        this.restaurant = restaurant;
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
