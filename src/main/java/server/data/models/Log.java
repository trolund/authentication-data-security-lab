package server.data.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Log", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class Log extends BaseEntity implements Serializable {

    public Log(Integer userID, String action) {
        this.userID = userID;
        this.action = action;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer logId;

    @Column(name = "userID", unique = false, nullable = false)
    private Integer userID;

    @Column(name = "Action", unique = false, nullable = false, length = 100)
    private String action;

}
