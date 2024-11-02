package cs203.ftms.user.security.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cs203.ftms.user.security.model.RefreshToken;
import cs203.ftms.user.security.repository.RefreshTokenRepository;
import cs203.ftms.user.model.User;
import cs203.ftms.user.repository.UserRepository;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${security.refreshtoken.expiration-time}")
    private long expiry;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken oldRT = user.getRefreshToken();
        if (oldRT != null) {
            refreshTokenRepository.delete(oldRT);
            // user.setRefreshToken(null);
        }
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), Instant.now().plusMillis(expiry), user);
        // RefreshToken.builder()
        //         .user(userRepository.findByEmail(username).orElse(null))
        //         .token(UUID.randomUUID().toString())
        //         .expiryDate(Instant.now().plusMillis(expiryTime)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file 
        //         .build();
        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByToken(String token){
        System.out.println("find by token service: "+token);
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        System.out.println("verify expiry service");
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}
