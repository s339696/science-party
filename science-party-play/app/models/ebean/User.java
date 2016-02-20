package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name="users")
public class User extends Model {

    // Finder
    public static Finder<Long,User> find = new Finder<>(User.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @Size(max=45)
    private String firstname;

    @Size(max=45)
    private String lastname;

    private Timestamp birthday;

    @Size(max=60)
    private String email;

    @Size(max=32)
    private String password;

    private int points;

    private boolean locked;

    @CreatedTimestamp
    @Column(name="date_created")
    private Timestamp whenCreated;

    @UpdatedTimestamp
    @Column(name="date_updated")
    private Timestamp whenUpdated;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
