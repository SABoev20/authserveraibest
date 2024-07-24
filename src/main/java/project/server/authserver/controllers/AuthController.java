package project.server.authserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.server.authserver.models.DTO.LoginRequest;
import project.server.authserver.models.User;
import project.server.authserver.models.User2;
import project.server.authserver.services.AuthService;
import project.server.authserver.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User2 user){
       /* if (!authService.isAdmin(authentication)) {
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
        }*/
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        if(authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())){
            return authService.generateToken(loginRequest.getEmail());
        }
        return "Login unsuccessful";
    }
}