package m1.info.reza.restaurant.DTO;

import jakarta.validation.constraints.*;

public class RestaurantCreateRequest {

    @NotEmpty(message = "le champ 'name' est requis.")
    @Size(min = 2, max = 100, message = "La taille maximale du champ 'name' doit être entre 2 et 100 caractères.")
    private String name;

    @NotEmpty(message = "le champ 'address' est requis.")
    @Size(min = 2, max = 100, message = "La taille maximale du champ 'address' doit être entre 2 et 100 caractères.")
    private String address;

    @NotEmpty(message = "le champ 'city' est requis.")
    @Size(min = 2, max = 100, message = "La taille maximale du champ 'city' doit être entre 2 et 100 caractères.")
    private String city;

    @Pattern(regexp = "\\d{5}", message = "Le champ 'zipcode' doit contenir exactement 5 chiffres.")
    private String zipcode;

    @Min(value = 1, message = "Le champ 'capacity' doit être au moins 1.")
    @Max(value = 1000, message = "Le champ 'capacity' ne peut pas dépasser 1000.")
    private int capacity;

    public RestaurantCreateRequest() {
    }

    public RestaurantCreateRequest(String name, String address, String city, String zipcode, int capacity) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.capacity = capacity;
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
