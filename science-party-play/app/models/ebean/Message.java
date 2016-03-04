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
public class Message {

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
}
