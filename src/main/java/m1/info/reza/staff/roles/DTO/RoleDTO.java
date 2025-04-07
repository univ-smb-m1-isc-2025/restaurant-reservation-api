package m1.info.reza.staff.roles.DTO;

import m1.info.reza.staff.roles.Role;

public class RoleDTO {

    private Long id;
    private String roleName;

    // Constructeur, Getters et Setters
    public RoleDTO(Role role){
        this.id = role.getId();
        this.roleName = role.getRoleName();
    }

    public RoleDTO(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
