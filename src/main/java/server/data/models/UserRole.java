package server.data.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UserRole", uniqueConstraints = { @UniqueConstraint(columnNames = "ID")})
public class UserRole extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer userRoleID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}
