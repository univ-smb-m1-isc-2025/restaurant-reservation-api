package m1.info.reza.user;

import jakarta.persistence.EntityNotFoundException;
import m1.info.reza.restaurant.Restaurant;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isPresent()) {
            return user.get();
        }

        throw new EntityNotFoundException("L'utilisateur avec l'email " + userEmail +" n'existe pas.");
    }
}
