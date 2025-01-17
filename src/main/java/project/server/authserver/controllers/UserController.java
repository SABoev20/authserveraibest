package project.server.authserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.server.authserver.models.DTO.RoleResponse;
import project.server.authserver.models.DTO.UserResponse;
import project.server.authserver.models.User;
import project.server.authserver.models.Role;
import project.server.authserver.services.AuthService;
import project.server.authserver.services.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(Authentication authentication) {
        if (!authService.isAdmin(authentication)) {
            return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
        }
        List<User> users = userService.listUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/current/role")
    public RoleResponse getCurrentRole(Authentication authentication){
        Role role = authService.getUserByToken(authentication).getRole();
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setName(role.getName());
        return roleResponse;
    }

    @GetMapping("/current")
    public UserResponse getCurrent(Authentication authentication){
        UserResponse userResponse = new UserResponse();
        User user = authService.getUserByToken(authentication);
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        return userResponse;
    }

    @PutMapping(value = "/current")
    public User update(@RequestBody User updatedUser, Authentication authentication) {
        updatedUser.setId(authService.getUserByToken(authentication).getId());
        return userService.updateUser(updatedUser);
    }

    @DeleteMapping("/current")
    public User delete(Authentication authentication){
        return userService.deleteUser(authService.getUserByToken(authentication));
    }


}
