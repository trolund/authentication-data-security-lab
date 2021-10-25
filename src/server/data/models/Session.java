package server.data.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Session", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class Session extends BaseEntity implements Serializable {

    private int hoursToAdd = 5;

    public Session() {
    }

    public Session(User user) {
        this.user = user;
    }

    public Session(int hoursToAdd, User user) {
        this.hoursToAdd = hoursToAdd;
        this.user = user;

        LocalDateTime dateStart = LocalDateTime.now();
        LocalDateTime dateStop = dateStart.plusHours(hoursToAdd);
        this.validTo = java.sql.Timestamp.valueOf(dateStop);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer sessionId;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "ValidTo", nullable = false)
    private Date validTo;

    public int getHoursToAdd() {
        return hoursToAdd;
    }

    public void setHoursToAdd(int hoursToAdd) {
        this.hoursToAdd = hoursToAdd;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "Session{" +
                "hoursToAdd=" + hoursToAdd +
                ", sessionId=" + sessionId +
                ", user=" + user +
                ", validTo=" + validTo +
                '}';
    }
}
