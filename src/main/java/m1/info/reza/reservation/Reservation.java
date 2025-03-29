package m1.info.reza.reservation;

import jakarta.persistence.*;
import m1.info.reza.customer.Customer;
import m1.info.reza.reservation.status.ReservationStatus;
import m1.info.reza.restaurant.Restaurant;

import java.time.LocalDateTime;


@Entity
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private LocalDateTime reservationDate;

    public Reservation() {
    }

    public Reservation(Customer customer, Restaurant restaurant, ReservationStatus reservationStatus, LocalDateTime reservationDate) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.reservationStatus = reservationStatus;
        this.reservationDate = reservationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDate;
    }

    public void setReservationDateTime(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }
}

