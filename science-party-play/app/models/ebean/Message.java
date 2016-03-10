package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * This object represents a message from a user in a chat.
 */
@Entity
@Table(name = "messages")
public class Message extends Model {

    // Finder
    public static Model.Finder<Long, Message> find = new Model.Finder<>(Message.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Chat chat;

    @Size(max = 1000)
    private String text;

    @CreatedTimestamp
    @Column(name = "date_created", columnDefinition = "datetime")
    private Timestamp whenCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

}
