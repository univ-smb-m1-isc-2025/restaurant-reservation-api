package m1.info.reza.staff.DTO;

import m1.info.reza.staff.roles.DTO.RoleDTO;
import m1.info.reza.staff.roles.Role;
import m1.info.reza.user.DTO.UserDTO;
import m1.info.reza.user.User;

public class RestaurantStaffDTO {

    private UserDTO user;
    private RoleDTO role;

    public RestaurantStaffDTO(User user, Role role) {
        this.user = new UserDTO(user);
        this.role = new RoleDTO(role);
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }
}
