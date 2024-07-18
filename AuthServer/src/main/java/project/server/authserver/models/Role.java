package project.server.authserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "UserRoles")
@Getter
@Setter
public class Role {
    @Id
    private String roleName;
}
