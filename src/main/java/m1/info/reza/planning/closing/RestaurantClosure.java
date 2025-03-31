package m1.info.reza.planning.closing;

import jakarta.persistence.*;
import m1.info.reza.planning.opening.RestaurantOpening;

import java.time.LocalDate;

@Entity
public class RestaurantClosure {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "opening_id")
    private RestaurantOpening opening;
    private LocalDate closureDate;

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
}
