package models.ebean;

import com.avaje.ebean.*;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import controllers.Friends;
import exception.UserException;
import exception.friends.FriendRequestException;
import javassist.expr.ExprEditor;
import util.Helper;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    /**
     * Creates a new User into the Database with given parameters.
     * @param firstname
     * @param lastname
     * @param birthday
     * @param email
     * @param password
     * @return
     * @throws UserException
     */
    public static User createUser(String firstname, String lastname, String birthday, String email, String password) throws UserException {
        User user = new User();

        /* TODO: Check data for validity */
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setBirthday(checkAndConvertBirthday(birthday));
        user.setEmail(checkEmail(email));
        user.setPassword(Helper.getMD5fromString(password));
        user.setLocked(false);
        user.setPoints(0);

        // Insert into Database
        try {
            user.insert();
        }catch (Exception e) {
            throw new UserException("Es konnte leider kein neuer User erstellt werden.");
        }

        return user;
    }

    /**
     * Checks if a mail address already exists in the database.
     *
     * @param email
     * @return
     * @throws UserException
     */
    private static String checkEmail(String email) throws UserException {
        //Check if email address already exist in DB.
        if (User.find.where().like("email",email).findUnique() != null) {
            throw new UserException("Es ist bereits ein User mit dieser Mailadresse vorhanden.");
        }
        //TODO: Check for format
        return email;
    }

    /**
     * Check format of a given birthday string and converts to Timestamp.
     * @param birthday
     * @return
     * @throws UserException
     */
    private static Timestamp checkAndConvertBirthday(String birthday) throws UserException {
        int day;
        int month;
        int year;

        if (!birthday.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new UserException("Der angegebene Geburtstag hat nicht das richtige Format (dd.mm.yyyy).");
        }
        String[] splitedBirth = birthday.split("\\.");
        day = Integer.parseInt(splitedBirth[0]);
        month = Integer.parseInt(splitedBirth[1]);
        year = Integer.parseInt(splitedBirth[2]);
        Timestamp ts =  Timestamp.valueOf(LocalDateTime.of(year,month,day,0,0));
        return ts;
    }


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

    /*
     * METHODS TO MANAGE GAMES OF A USER
     */

    /**
     * Returns a list with all pending games for the user.
     *
     * @return
     */
    public List<Game> getPendingGames() {
        return Game.find
                .where()
                .ieq("game_status", "P")
                .eq("players.user", this)
                .ne("player_status", "L")
                .ne("player_status", "D")
                .findList();
    }

    /**
     * Returns a list with all running games for this user
     *
     * @return
     */
    public List<Game> getRunningGames() {
        List<Game> runningGames = Game.find
                .fetch("players")
                .where()
                .ilike("game_status", "A")
                .eq("user_id", this.getId())
                .ne("player_status", "L")
                .findList();

        return runningGames;
    }

    /*
     * METHODS TO MANAGE FRIENDS OF A USER
     */

    public Friend sendFriendRequestTo(User to) throws FriendRequestException {
        // Proof if there is already a friendship or request
        Friend friend = getFriendshipOrRequestWith(to);
        if (friend != null) {
            throw new FriendRequestException("Es gibt bereits eine Freundschaft oder eine Freundschaftsanfrage zwischen diesen beiden Usern.");
        }

        friend = new Friend();
        friend.setRequest(true);
        friend.setUserSendReq(this);
        friend.setUserGetReq(to);
        friend.insert();

        return friend;
    }

    public void responeFriendRequestFrom(User from, boolean accept) throws FriendRequestException {
        Friend friendRequest = Friend.find.where()
                .ieq("request", "1")
                .ieq("user_get_req_id", this.getId().toString())
                .ieq("user_send_req_id", from.getId().toString())
                .findUnique();

        if (friendRequest == null) {
            throw new FriendRequestException("Es gibt keine Freundschaftsanfrage von " + from.toString() + " an " + this.toString() + ".");
        } else {
            if (accept == true) {
                friendRequest.setRequest(false);
                friendRequest.update();
                throw new FriendRequestException("Die Freundschaftsanfrage wurde angenommen.");
            } else {
                friendRequest.delete();
                throw new FriendRequestException("Die Freundschaftsanfrage wurde abgelehnt.");
            }
        }
    }

    public List<User> getFriendRequests() {
        List<Friend> requestFriends = Friend.find.where()
                .ieq("request", "1")
                .ieq("user_get_req_id", this.getId().toString())
                .findList();

        List<User> requestUsers = new ArrayList<User>();
        for (Friend friend : requestFriends) {
            requestUsers.add(friend.getUserSendReq());
        }

        return requestUsers;
    }

    /**
     * Returns all friends from a user.
     *
     * @return
     */
    public List<User> getFriends() {

        List<Friend> friends = Friend.find.where()
                .ieq("request", "0")
                .or(Expr.ieq("user_get_req_id", this.getId().toString()),
                        Expr.ieq("user_send_req_id", this.getId().toString()))
                .findList();

        // Add users which are friends to a userlist.
        List<User> friendUsers = new ArrayList<User>();

        for (Friend friend : friends) {
            if (!friend.getUserGetReq().equals(this)) {
                friendUsers.add(friend.getUserGetReq());
            } else if (!friend.getUserSendReq().equals(this)) {
                friendUsers.add(friend.getUserSendReq());
            }
        }

        // TODO: Sort list by firstname

        return friendUsers;
    }

    public Friend getFriendshipWith(User user) {
        Friend friend = getFriendshipOrRequestWith(user);
        if (friend.isRequest() == true) {
            return null;
        } else {
            return friend;
        }
    }

    private Friend getFriendshipOrRequestWith(User user) {
        Expression friendship1 = Expr.and(Expr.ieq("user_get_req_id", this.getId().toString()),
                Expr.ieq("user_send_req_id", user.getId().toString()));
        Expression friendship2 = Expr.and(Expr.ieq("user_send_req_id", this.getId().toString()),
                Expr.ieq("user_get_req_id", user.getId().toString()));
        Friend friend = Friend.find.where()
                .or(friendship1, friendship2)
                .findUnique();

        return friend;
    }

    @Override
    public String toString() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
