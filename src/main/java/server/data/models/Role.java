package server.data.models;

import shared.dto.Roles;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Role", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class Role extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer roleId;

    @Column(name = "Role", unique = true, nullable = false)
    private Roles role;
}
