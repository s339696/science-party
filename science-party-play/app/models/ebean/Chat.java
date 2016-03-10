package models.ebean;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * A chat between two or more person
 */
@Entity
@Table(name = "chats")
public class Chat {

    // Finder
    public static Model.Finder<Long, Chat> find = new Model.Finder<>(Chat.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 50)
    private String name;

    @JoinTable(name = "user_has_chats")
    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;
}
