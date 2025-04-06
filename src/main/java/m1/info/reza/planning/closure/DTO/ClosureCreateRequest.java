package m1.info.reza.planning.closure.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ClosureCreateRequest {

    @NotNull(message = "L'ID d'ouverture est requis.")
    private Long openingId;

    @FutureOrPresent(message = "La date de fermeture ne peut pas être dans le passé.")
    private LocalDate dateOfClosure;

    public ClosureCreateRequest(Long openingId, LocalDate dateOfClosure) {
        this.openingId = openingId;
        this.dateOfClosure = dateOfClosure;
    }

    public Long getOpeningId() {
        return openingId;
    }

    public void setOpeningId(Long openingId) {
        this.openingId = openingId;
    }

    public LocalDate getDateOfClosure() {
        return dateOfClosure;
    }

    public void setDateOfClosure(LocalDate dateOfClosure) {
        this.dateOfClosure = dateOfClosure;
    }
}
