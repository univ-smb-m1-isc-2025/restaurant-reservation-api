package m1.info.reza.auth;

import m1.info.reza.auth.DTO.LoginUserDto;
import m1.info.reza.auth.DTO.RegisterUserDto;
import m1.info.reza.user.User;
import m1.info.reza.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new RuntimeException("Cet e-mail est déjà utilisé.");
        }

        User user = new User(
            input.getEmail(),
            passwordEncoder.encode(input.getPassword()),
            input.getFirstName(),
            input.getLastName()
        );

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou mot de passe invalide."));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
            );
        } catch (BadCredentialsException  e) {
            throw new BadCredentialsException("E-mail ou mot de passe invalide.");
        }

        return user;
    }
}