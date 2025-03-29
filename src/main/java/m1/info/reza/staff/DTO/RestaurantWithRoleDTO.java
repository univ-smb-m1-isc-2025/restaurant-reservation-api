package m1.info.reza.staff.DTO;

import m1.info.reza.restaurant.DTO.RestaurantDTO;
import m1.info.reza.staff.roles.DTO.RoleDTO;

public class RestaurantWithRoleDTO {

    private RestaurantDTO restaurant;
    private RoleDTO role;

    // Constructeur, Getters et Setters
    public RestaurantWithRoleDTO(RestaurantDTO restaurant, RoleDTO role) {
        this.restaurant = restaurant;
        this.role = role;
    }

    // Getters et Setters
    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }
}
