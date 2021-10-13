package server.data.models;

import org.hibernate.annotations.OptimisticLockType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Job", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer employeeId;

    @Column(name = "FILE_NAME", unique = true, nullable = false, length = 100)
    private String fileName;

    @Column(name = "PRINTER", unique = false, nullable = false, length = 100)
    private String printer;

}
