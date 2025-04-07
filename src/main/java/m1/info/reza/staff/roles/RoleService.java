package m1.info.reza.staff.roles;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);

        if (role.isPresent()) {
            return role.get();
        }

        throw new EntityNotFoundException("Le rôle avec l'id" + roleId +" n'existe pas.");
    }

    public Role getOwnerRole() {
        return roleRepository.findByRoleName("OWNER")
                .orElseThrow(() -> new EntityNotFoundException("Le rôle 'OWNER' n'existe pas en base de données !"));
    }

    public Role getManagerRole() {
        return roleRepository.findByRoleName("MANAGER")
                .orElseThrow(() -> new EntityNotFoundException("Le rôle 'MANAGER' n'existe pas en base de données !"));
    }

    public Role getEmployeeRole() {
        return roleRepository.findByRoleName("EMPLOYEE")
                .orElseThrow(() -> new EntityNotFoundException("Le rôle 'EMPLOYEE' n'existe pas en base de données !"));
    }
}