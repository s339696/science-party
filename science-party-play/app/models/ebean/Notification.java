package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

/**
 * Represents a notification
 */

@Entity
@Table(name = "notifications")
public class Notification extends Model {

    // Finder
    public static Finder<Long, Notification> find = new Finder<>(Notification.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 255)
    String privateText;

    @Size(max = 255)
    String publicText;

    boolean publicAvailable;

    boolean privateSeen;

    @ManyToOne
    private User user;

    @CreatedTimestamp
    @Column(name = "date_created", columnDefinition = "datetime")
    private Timestamp whenCreated;

    public static Notification createNotification(User user, String privateText, String publicText) {
        Notification not = new Notification();
        not.setUser(user);
        not.setPrivateSeen(false);
        not.setPrivateText(privateText);
        if (publicText.equals("")) {
            not.setPublicText("");
            not.setPublicAvailable(false);
        } else {
            not.setPublicText(publicText);
            not.setPublicAvailable(true);
        }

        not.insert();

        return not;
    }

    /**
     * Returns the number of unseen Notifications for a user.
     *
     * @param user
     * @return
     */
    public static int getUnseenNotificationCount(User user) {
        return getUnseenNotifications(user).size();
    }

    /**
     * Returns a list of unseen notifications for a user
     *
     * @param user
     * @return
     */
    public static List<Notification> getUnseenNotifications(User user) {
        return find.where()
                .ieq("user_id", user.getId().toString())
                .eq("privateSeen", false)
                .orderBy().desc("whenCreated")
                .findList();
    }

    /**
     * Returns a list of unseen notifications for a user
     *
     * @param user
     * @return
     */
    public static List<Notification> getSeenNotifications(User user) {
        return find.where()
                .ieq("user_id", user.getId().toString())
                .eq("privateSeen", true)
                .orderBy().desc("whenCreated")
                .findList();
    }

    /**
     * Returns all notifications for a special user.
     *
     * @param user
     * @return
     */
    public static List<Notification> getNotifications(User user) {
        return find.where()
                .ieq("user_id", user.getId().toString())
                .orderBy().desc("whenCreated")
                .findList();
    }

    /**
     * Returns all public notifications ordered by time
     *
     * @return
     */
    public static List<Notification> getPublicNotifications() {
        return find.where()
                .eq("publicAvailable", true)
                .orderBy().desc("whenCreated")
                .findList();
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Timestamp whenCreated) {
        this.whenCreated = whenCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPrivateSeen() {
        return privateSeen;
    }

    public void setPrivateSeen(boolean privateSeen) {
        this.privateSeen = privateSeen;
    }

    public boolean isPublicAvailable() {
        return publicAvailable;
    }

    public void setPublicAvailable(boolean publicAvailable) {
        this.publicAvailable = publicAvailable;
    }

    public String getPublicText() {
        return publicText;
    }

    public void setPublicText(String publicText) {
        this.publicText = publicText;
    }

    public String getPrivateText() {
        return privateText;
    }

    public void setPrivateText(String privateText) {
        this.privateText = privateText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
