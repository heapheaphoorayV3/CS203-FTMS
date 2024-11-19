package cs203.ftms.overall.controller.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs203.ftms.overall.dto.AuthenticationDTO;
import cs203.ftms.overall.dto.JwtDTO;
import cs203.ftms.overall.dto.RegisterAdminDTO;
import cs203.ftms.overall.dto.RegisterFencerDTO;
import cs203.ftms.overall.dto.RegisterOrganiserDTO;
import cs203.ftms.overall.dto.ResetPasswordDTO;
import cs203.ftms.overall.exception.EntityDoesNotExistException;
import cs203.ftms.overall.model.userrelated.Fencer;
import cs203.ftms.overall.model.userrelated.Organiser;
import cs203.ftms.overall.model.userrelated.User;
import cs203.ftms.overall.security.model.RefreshToken;
import cs203.ftms.overall.security.service.JwtService;
import cs203.ftms.overall.security.service.RefreshTokenService;
import cs203.ftms.overall.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

/**
 * Controller handling authentication-related endpoints for user registration, login, and password management.
 * Provides REST endpoints for user registration (fencer, organiser, admin), login, token refresh, and password reset functionality.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService; 


    /**
     * Controller class responsible for handling authentication-related HTTP requests.
     * 
     * @param authenticationService Service handling authentication operations
     * @param jwtService Service handling JWT token operations
     * @param refreshTokenService Service handling refresh token operations
     */
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;

    }

    /**
     * Registers a new fencer in the system.
     * 
     * @param registerFencerDTO Data transfer object containing fencer registration information
     * @return ResponseEntity containing a success/failure message and appropriate HTTP status code:
     *         201 CREATED if registration is successful,
     *         400 BAD REQUEST if registration fails
     */
    @PostMapping("/register-fencer")
    public ResponseEntity<String> createFencer(@Valid @RequestBody RegisterFencerDTO registerFencerDTO) {
        User newUser = authenticationService.createFencer(registerFencerDTO);
        if (newUser != null) {
            return new ResponseEntity<>("registration successful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the registration of a new organiser user in the system.
     * 
     * @param registerOrganiserDTO The DTO containing the organiser registration information
     * @return ResponseEntity containing a success/failure message and appropriate HTTP status
     *         Returns HTTP 201 CREATED with success message if registration is successful
     *         Returns HTTP 400 BAD REQUEST with failure message if registration fails
     */
    @PostMapping("/register-organiser")
    public ResponseEntity<String> createOrganiser(@Valid @RequestBody RegisterOrganiserDTO registerOrganiserDTO) {
        User newUser = authenticationService.createOrganiser(registerOrganiserDTO);
        if (newUser != null) {
            return new ResponseEntity<>("registration successful", HttpStatus.CREATED);
        } 
        return new ResponseEntity<>("registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a new admin user based on the provided registration data.
     * 
     * @param registerAdminDTO Data Transfer Object containing admin registration information
     * @return ResponseEntity containing registration status message:
     *         201 Created if registration is successful
     *         400 Bad Request if registration fails
     */
    @PostMapping("/register-admin")
    public ResponseEntity<String> createAdmin(@Valid @RequestBody RegisterAdminDTO registerAdminDTO) {
        User newUser = authenticationService.createAdmin(registerAdminDTO);
        if (newUser != null) {
            return new ResponseEntity<>("registration successful", HttpStatus.CREATED);
        } 
        return new ResponseEntity<>("registration unsuccessful", HttpStatus.BAD_REQUEST);
    }

    /**
     * Authenticates a user and generates JWT tokens for authorization.
     * 
     * @param authenticationDTO DTO containing user email and password credentials
     * @return ResponseEntity containing JWT token, refresh token, expiration time, and user type
     * @throws ValidationException if input validation fails
     */
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

    /**
     * Refreshes the JWT access token using a provided refresh token.
     * 
     * @param token The refresh token string used to generate a new access token
     * @return ResponseEntity containing a JwtDTO with the new access token and related information
     * @throws EntityDoesNotExistException if the refresh token is not found in the database
     */
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
                }).orElseThrow(() ->new EntityDoesNotExistException("Refresh Token is not in DB..!!"));
    }

    /**
     * Handles the forget password request for a user.
     * 
     * @param email The email address of the user requesting password reset
     * @return ResponseEntity containing a message about the password reset status
     *         with HTTP status 200 (OK) if successful
     * @throws IllegalArgumentException if the email is invalid
     */
    @PutMapping("/forget-password/{email}")
    public ResponseEntity<String> forgetPassword(@PathVariable String email) {
        return new ResponseEntity<>(authenticationService.forgetPassword(email), HttpStatus.OK);
    }

    /**
     * Resets a user's password using a provided reset token.
     * 
     * @param token The password reset token that validates the reset request
     * @param resetPasswordDTO Contains the new password to be set
     * @return {@code ResponseEntity<String>} with status OK and success/failure message
     */
    @PutMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return new ResponseEntity<>(authenticationService.resetPassword(token, resetPasswordDTO.getNewPassword()), HttpStatus.OK);
    }
}
