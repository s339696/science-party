package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import controllers.Friends;
import util.Helper;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Column(columnDefinition = "datetime")
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
    @Column(name = "date_created", columnDefinition = "datetime")
    private Timestamp whenCreated;

    @UpdatedTimestamp
    @Column(name = "date_updated", columnDefinition = "datetime")
    private Timestamp whenUpdated;

    @Column(name = "last_online", columnDefinition = "datetime")
    private Timestamp lastOnline;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PerkPerUserAndTopic> perksPerUserAndTopic;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToMany(mappedBy = "userSendReq", cascade = CascadeType.ALL)
    private List<Friend> friendsSendReq;

    @OneToMany(mappedBy = "userGetReq", cascade = CascadeType.ALL)
    private List<Friend> friendsGetReq;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages;

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

    public Friend sendFriendRequest(User to) {
        Friend friend = new Friend();
        friend.setRequest(true);
        friend.setUserSendReq(this);
        friend.setUserGetReq(to);
        friend.insert();

        return friend;
    }

    public List<User> getFriendRequests() {
        List<Friend> requestFriends = Friend.find.where()
                .ieq("request", "1")
                .ieq("user_get_req_id", this.getId().toString())
                .findList();

        List<User> requestUsers = new ArrayList<User>();
        for (Friend friend: requestFriends) {
            requestUsers.add(friend.getUserSendReq());
        }

        return requestUsers;
    }

    public List<User> getFriends() {
        return null;
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
