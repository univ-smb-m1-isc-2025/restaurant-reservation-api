package m1.info.reza.staff;

import jakarta.persistence.*;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.user.User;

@Entity
public class RestaurantStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public RestaurantStaff() {
    }

    public RestaurantStaff(User user, Restaurant restaurant, Role role) {
        this.user = user;
        this.restaurant = restaurant;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
