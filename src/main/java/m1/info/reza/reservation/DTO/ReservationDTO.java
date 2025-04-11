package m1.info.reza.reservation.DTO;

import m1.info.reza.customer.Customer;
import m1.info.reza.reservation.Reservation;
import m1.info.reza.reservation.status.ReservationStatus;

import java.time.LocalDateTime;

public class ReservationDTO {

    private Long id;
    private Customer customer;
    private int nbGuests;
    private ReservationStatus reservationStatus;
    private LocalDateTime reservationDate;

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.customer = reservation.getCustomer();
        this.nbGuests = reservation.getNbGuests();
        this.reservationStatus = reservation.getReservationStatus();
        this.reservationDate = reservation.getReservationDate();
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

    public int getNbGuests() {
        return nbGuests;
    }

    public void setNbGuests(int nbGuests) {
        this.nbGuests = nbGuests;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }
}
