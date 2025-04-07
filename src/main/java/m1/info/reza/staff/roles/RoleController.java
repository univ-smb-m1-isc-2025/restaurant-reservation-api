package m1.info.reza.staff.roles;

import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.staff.DTO.RestaurantStaffDTO;
import m1.info.reza.staff.roles.DTO.RoleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RoleDTO>>> index(){

        List<Role> roles = roleService.getRoles();

        List<RoleDTO> roleDTOs = roles.stream()
                .map(RoleDTO::new)
                .toList();

        ApiResponse<List<RoleDTO>> response = ResponseUtil.success("La liste des rôles a été récupéree avec succès.", roleDTOs);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
