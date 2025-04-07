package m1.info.reza.staff.DTO;

public class CreateStaffRequest {

    private String userEmail;
    private Long roleId;

    public CreateStaffRequest(String userEmail, Long roleId) {
        this.userEmail = userEmail;
        this.roleId = roleId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
