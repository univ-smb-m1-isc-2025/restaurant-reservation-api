package m1.info.reza.planning.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class OpeningHoursRequest {
    @NotNull(message = "Le jour ne peut pas être nul")
    private DayOfWeek day;

    @NotNull(message = "L'heure d'ouverture ne peut pas être nulle")
    private LocalTime openingTime;

    @NotNull(message = "L'heure de fermeture ne peut pas être nulle")
    private LocalTime closingTime;

    public OpeningHoursRequest(DayOfWeek day, LocalTime openingTime, LocalTime closingTime) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
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