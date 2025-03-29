package m1.info.reza.staff.roles;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.restaurant.Restaurant;
import m1.info.reza.user.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getOwnerRole() {
        return roleRepository.findByRoleName("OWNER")
                .orElseThrow(() -> new EntityNotFoundException("Le rôle 'Owner' n'existe pas en base de données !"));
    }
}