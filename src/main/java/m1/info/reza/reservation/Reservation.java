package m1.info.reza.reservation;

import jakarta.persistence.*;
import m1.info.reza.customer.Customer;
import m1.info.reza.reservation.status.ReservationStatus;
import m1.info.reza.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private int nbGuests;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    private LocalDateTime reservationDate;

    public Reservation() {
    }

    public Reservation(Restaurant restaurant, Customer customer, LocalDateTime reservationDate, int nbGuests) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.nbGuests = nbGuests;
        this.reservationDate = reservationDate;
        this.reservationStatus = ReservationStatus.PENDING;
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

    public int getNbGuests() {
        return nbGuests;
    }

    public void setNbGuests(int nbGuests) {
        this.nbGuests = nbGuests;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getFormattedFrenchDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.FRENCH);
        String formatted = reservationDate.format(formatter);
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }

    public String getFormattedFrenchTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'h'mm");
        return reservationDate.format(formatter);
    }
}

