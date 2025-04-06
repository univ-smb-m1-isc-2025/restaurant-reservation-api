package m1.info.reza.planning.closure;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import m1.info.reza.planning.opening.RestaurantOpening;

import java.time.LocalDate;

@Entity
public class RestaurantClosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate closureDate;

    @ManyToOne
    @JoinColumn(name = "opening_id")
    @JsonBackReference
    private RestaurantOpening opening;


    public RestaurantOpening getOpening() {
        return opening;
    }

    public void setOpening(RestaurantOpening opening) {
        this.opening = opening;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(LocalDate closureDate) {
        this.closureDate = closureDate;
    }
}
