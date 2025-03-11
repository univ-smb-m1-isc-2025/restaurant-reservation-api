package m1.info.reza.auth;


import jakarta.validation.Valid;
import m1.info.reza.auth.DTO.AuthResponseDto;
import m1.info.reza.auth.DTO.LoginUserDto;
import m1.info.reza.auth.DTO.RegisterUserDto;
import m1.info.reza.configs.JwtService;
import m1.info.reza.response.ApiResponse;
import m1.info.reza.response.ResponseUtil;
import m1.info.reza.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authenticationService;

    public AuthController(JwtService jwtService, AuthService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        String jwtToken = jwtService.generateToken(registeredUser);

        AuthResponseDto responseDto = new AuthResponseDto(registeredUser, jwtToken);

        ApiResponse<AuthResponseDto> response = ResponseUtil.success("Utilisateur inscrit avec succès.", responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        AuthResponseDto responseDto = new AuthResponseDto(authenticatedUser, jwtToken);

        ApiResponse<AuthResponseDto> response = ResponseUtil.success("Utilisateur connecté avec succès.", responseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}