package m1.info.reza.reservation.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class CreateReservationRequest {

    @NotNull(message = "La date de réservation est requise")
    @Future(message = "La date de réservation doit être dans le futur")
    private LocalDateTime reservationDate;

    @NotBlank(message = "L'email du client est requis")
    @Email(message = "Veuillez renseigner un e-mail valide.")
    private String email;

    @Min(value = 1, message = "Il doit y avoir au moins 1 invité")
    private int nbGuests;

    public CreateReservationRequest(LocalDateTime reservationDate, String email, int nbGuests) {
        this.reservationDate = reservationDate;
        this.email = email;
        this.nbGuests = nbGuests;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getCustomerEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNbGuests() {
        return nbGuests;
    }

    public void setNbGuests(int nbGuests) {
        this.nbGuests = nbGuests;
    }
}
