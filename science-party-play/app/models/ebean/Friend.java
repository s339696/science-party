package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Respresents a friendship in the database.
 */
@Entity
@Table(name="friends")
public class Friend extends Model {

    // Finder
    public static Finder<Long, Friend> find = new Finder<>(Friend.class);

    // Columns
    @ManyToOne
    private User userSendReq;

    @ManyToOne
    private User userGetReq;

    private boolean request;

    @CreatedTimestamp
    @Column(name = "date_request", columnDefinition = "datetime")
    private Timestamp whenRequest;

    @UpdatedTimestamp
    @Column(name = "date_friends", columnDefinition = "datetime")
    private Timestamp whenFriends;

    public User getUserSendReq() {
        return userSendReq;
    }

    public void setUserSendReq(User userSendReq) {
        this.userSendReq = userSendReq;
    }

    public User getUserGetReq() {
        return userGetReq;
    }

    public void setUserGetReq(User userGetReq) {
        this.userGetReq = userGetReq;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public Timestamp getWhenRequest() {
        return whenRequest;
    }

    public void setWhenRequest(Timestamp whenRequest) {
        this.whenRequest = whenRequest;
    }

    public Timestamp getWhenFriends() {
        return whenFriends;
    }

    public void setWhenFriends(Timestamp whenFriends) {
        this.whenFriends = whenFriends;
    }
}
