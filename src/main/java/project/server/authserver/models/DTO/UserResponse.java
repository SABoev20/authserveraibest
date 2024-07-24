package project.server.authserver.models.DTO;

import lombok.Getter;
import lombok.Setter;
import project.server.authserver.models.Role;
@Setter
@Getter
public class UserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
