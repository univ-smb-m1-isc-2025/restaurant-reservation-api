package m1.info.reza.restaurant.DTO;

import m1.info.reza.planning.RestaurantOpening;
import m1.info.reza.planning.closure.RestaurantClosure;
import m1.info.reza.restaurant.Restaurant;

import java.util.List;

public class RestaurantDTO {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String zipcode;
    private int capacity;
    private List<RestaurantOpening> openings;

    // Constructeur, Getters et Setters
    public RestaurantDTO(Long id, String name, String address, String city, String zipcode, int capacity, List<RestaurantOpening> restaurantOpenings) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.capacity = capacity;
        this.openings = restaurantOpenings;
    }

    public RestaurantDTO(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.city = restaurant.getCity();
        this.zipcode = restaurant.getZipcode();
        this.capacity = restaurant.getCapacity();
        this.openings = restaurant.getOpenings();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<RestaurantOpening> getOpenings() {
        return openings;
    }
}
