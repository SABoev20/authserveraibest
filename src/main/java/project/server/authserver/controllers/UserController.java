package project.server.authserver.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.server.authserver.models.User;
import project.server.authserver.services.AuthService;
import project.server.authserver.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "User Management System", description = "Operations pertaining to users in the User Management System")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }
    @GetMapping("/users")
    @ApiOperation(value = "Get all users", response = List.class)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.listUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/current")
    @ApiOperation(value = "Get current authenticated user", response = User.class)
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        User user = authService.getUserByToken(authentication);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/current")
    @ApiOperation(value = "Update current authenticated user", response = User.class)
    public ResponseEntity<User> updateCurrentUser(@RequestBody User updatedUser, Authentication authentication) {
        updatedUser.setId(authService.getUserByToken(authentication).getId());
        User user = userService.updateUser(updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/current")
    @ApiOperation(value = "Delete current authenticated user", response = User.class)
    public ResponseEntity<User> deleteCurrentUser(Authentication authentication) {
        User user = userService.deleteUser(authService.getUserByToken(authentication));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all users for admin", response = List.class)
    public ResponseEntity<?> getAll(Authentication authentication) {
        if (!authService.isAdmin(authentication)) {
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
        }
        List<User> users = userService.listUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
