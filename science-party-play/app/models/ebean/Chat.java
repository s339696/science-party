package models.ebean;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import exception.messages.CreateMessageException;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * A chat between two or more person
 */
@Entity
@Table(name = "chats")
public class Chat extends Model {

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

    public static void createChat(User from, List<User> members, String name, String firstMsg) throws CreateMessageException {
        Ebean.beginTransaction();
        try {
            Chat chat = new Chat();
            members.add(0,from);
            chat.setUsers(members);
            chat.setName(name);
            chat.save();

            Message newMsg = new Message();
            newMsg.setUser(from);
            newMsg.setText(firstMsg);
            newMsg.setChat(chat);
            newMsg.save();

            Ebean.commitTransaction();
        } catch (Exception e){
            throw new CreateMessageException("Die Nachricht konnte nicht erzeugt werden.");
        } finally {
            Ebean.endTransaction();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
