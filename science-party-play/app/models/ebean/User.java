package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import util.Helper;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends Model {

    // Finder
    public static Finder<Long, User> find = new Finder<>(User.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 45)
    private String firstname;

    @Size(max = 45)
    private String lastname;

    private Timestamp birthday;

    @Size(max = 60)
    @Column(unique = true)
    private String email;

    @Size(max = 32)
    private String password;

    private boolean author;

    private int points;

    private boolean locked;

    @CreatedTimestamp
    @Column(name = "date_created")
    private Timestamp whenCreated;

    @UpdatedTimestamp
    @Column(name = "date_updated")
    private Timestamp whenUpdated;

    @Column(name = "last_online")
    private Timestamp lastOnline;

    @JoinTable(name = "user_has_perks")
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Perk> perks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Player> players;

    public Long getId() {
        return id;
    }

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

    public String getBirthdayAsString() {
        return Helper.getStringFromTimestamp(getBirthday(), "dd.MM.YYYY");
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

    public Timestamp getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Timestamp lastOnline) {
        this.lastOnline = lastOnline;
    }

    public List<Perk> getPerks() {
        return perks;
    }

    public void setPerks(List<Perk> perks) {
        this.perks = perks;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Timestamp getWhenUpdated() {
        return whenUpdated;
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
