package m1.info.reza.configs.seeder;

import m1.info.reza.staff.roles.Role;
import m1.info.reza.staff.roles.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        List<String> roles = List.of("OWNER", "MANAGER", "EMPLOYEE");

        for (String roleName : roles) {
            if (roleRepository.findByRoleName(roleName).isEmpty()) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);

                System.out.println("✅ Rôle ajouté : " + roleName);
            }
        }
    }
}
