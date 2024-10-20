package cs203.ftms.overall.controller.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.AuthenticationDTO;
import cs203.ftms.overall.dto.JwtDTO;
import cs203.ftms.overall.dto.RefreshTokenRequestDTO;
import cs203.ftms.overall.dto.RegisterAdminDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.security.model.RefreshToken;
import cs203.ftms.overall.security.service.JwtService;
import cs203.ftms.overall.security.service.RefreshTokenService;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService; 


    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;

    }

    @PostMapping("/register-fencer")
    public ResponseEntity<String> createFencer(@Valid @RequestBody RegisterFencerDTO f) {
        User newUser = authenticationService.createFencer(f);
        if (newUser != null) {
            return new ResponseEntity<>("registration successful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register-organiser")
    public ResponseEntity<String> createOrganiser(@Valid @RequestBody RegisterOrganiserDTO o) {
        User newUser = authenticationService.createOrganiser(o);
        if (newUser != null) {
            return new ResponseEntity<>("registration successful", HttpStatus.CREATED);
        } 
        return new ResponseEntity<>("registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> createAdmin(@Valid @RequestBody RegisterAdminDTO a) {
        User newUser = authenticationService.createAdmin(a);
        if (newUser != null) {
            return new ResponseEntity<>("registration successful", HttpStatus.CREATED);
        } 
        return new ResponseEntity<>("registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        User user = authenticationService.authenticateUser(authenticationDTO.getEmail(), authenticationDTO.getPassword());
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();
        char userType = 'A';
        if (user instanceof Organiser) {
            userType = 'O';
        } else if (user instanceof Fencer) {
            userType = 'F';
        }
        return new ResponseEntity<>(new JwtDTO("login success", jwtToken, jwtService.getExpirationTime(), userType, refreshToken), HttpStatus.OK);
    }

    @GetMapping("/refreshToken/{token}")
    public ResponseEntity<JwtDTO> refreshToken(@PathVariable String token){
        RefreshToken rf = refreshTokenService.findByToken(token).orElse(null);
        System.out.println(rf);
        return refreshTokenService.findByToken(token)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    char userType = 'A';
                    if (user instanceof Organiser) {
                        userType = 'O';
                    } else if (user instanceof Fencer) {
                        userType = 'F';
                    }
                    return new ResponseEntity<>(new JwtDTO("refreshed token", accessToken, jwtService.getExpirationTime(), userType, token), HttpStatus.OK);
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }
}
