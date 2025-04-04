package m1.info.reza.restaurant;

import jakarta.persistence.*;
import m1.info.reza.staff.RestaurantStaff;

import java.util.Set;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String city;
    private String zipcode;
    private int capacity;

    @OneToMany(mappedBy = "restaurant")
    private Set<RestaurantStaff> restaurantStaffs;

    public Restaurant() {
    }

    public Restaurant(String name, String address, String city, String zipcode, int capacity) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
