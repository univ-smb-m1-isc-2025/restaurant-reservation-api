package m1.info.reza.staff.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateStaffRequest {


    @NotNull(message = "Le userId est requis.")
    @Positive(message = "Le userId doit être un nombre positif.")
    private Long userId;

    @NotNull(message = "Le roleId est requis.")
    @Positive(message = "Le roleId doit être un nombre positif.")
    private Long roleId;

    public UpdateStaffRequest(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
