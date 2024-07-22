package project.server.authserver.controllers;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.server.authserver.models.DTO.LoginRequest;
import project.server.authserver.models.User;
import project.server.authserver.services.AuthService;
import project.server.authserver.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Api(value = "Authentication System", description = "Operations for user registration and login")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register a new user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered user"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<?> register(
            @ApiParam(value = "User object to be registered", required = true) @RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation(value = "Login a user and generate a token", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully logged in"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<String> login(
            @ApiParam(value = "Login request with email and password", required = true) @RequestBody LoginRequest loginRequest) {
        if(authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())){
            return new ResponseEntity<>(authService.generateToken(loginRequest.getEmail()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Login unsuccessful", HttpStatus.UNAUTHORIZED);
    }
}