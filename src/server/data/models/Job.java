package server.data.models;

import org.hibernate.annotations.OptimisticLockType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Job", uniqueConstraints = { @UniqueConstraint(columnNames = "ID") })
public class Job extends BaseEntity implements Serializable {

    public Job(Integer jobID, String fileName, String printer) {
        this.jobID = jobID;
        this.fileName = fileName;
        this.printer = printer;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    private Integer jobID;

    @Column(name = "FileName", unique = true, nullable = false, length = 100)
    private String fileName;

    @Column(name = "Printer", unique = false, nullable = false, length = 100)
    private String printer;

}
