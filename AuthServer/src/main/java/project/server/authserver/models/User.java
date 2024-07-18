package project.server.authserver.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    @OneToOne
    @JoinColumn(name = "RoleName", referencedColumnName = "RoleName")
    private Role role;

    private String password;

    private boolean isActive;
}
