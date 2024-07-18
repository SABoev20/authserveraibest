package project.server.authserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import project.server.authserver.models.Role;
import project.server.authserver.models.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtEncoder encoder;

    @Autowired
    public AuthService(UserService userService, JwtEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(String email, String password) {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByEmail(email));
        return optionalUser.filter(user -> passwordEncoder.matches(password, user.getPassword())).isPresent();
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(username)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public User getUserByToken(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return userService.findByEmail(jwt.getClaim("sub"));
    }

    public boolean isAdmin(Authentication authentication){
        return getUserByToken(authentication).getRole().getRoleName().equals("Admin");
    }
}
