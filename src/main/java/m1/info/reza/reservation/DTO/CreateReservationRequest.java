package m1.info.reza.reservation.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class CreateReservationRequest {

    @NotNull(message = "La date de réservation est requise")
    @Future(message = "La date de réservation doit être dans le futur")
    private LocalDateTime reservationDate;

    @NotBlank(message = "Le numéro de téléphone du client est requis")
    @Pattern(
            regexp = "^0[1-9]\\d{8}$",
            message = "Le numéro de téléphone doit être un numéro valide comme 0679554417"
    )
    private String customerPhone;

    @Min(value = 1, message = "Il doit y avoir au moins 1 invité")
    private int nbGuests;

    public CreateReservationRequest(LocalDateTime reservationDate, String customerPhone, int nbGuests) {
        this.reservationDate = reservationDate;
        this.customerPhone = customerPhone;
        this.nbGuests = nbGuests;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getNbGuests() {
        return nbGuests;
    }

    public void setNbGuests(int nbGuests) {
        this.nbGuests = nbGuests;
    }
}
