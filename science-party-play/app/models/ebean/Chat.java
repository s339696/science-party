package models.ebean;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import exception.messages.CreateMessageException;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    Timestamp lastMessage;

    @JoinTable(name = "user_has_chats")
    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    public static Chat createChat(User from, List<User> members, String name, String firstMsg) throws CreateMessageException {
        Ebean.beginTransaction();
        Chat chat = new Chat();
        try {
            members.add(0,from);
            chat.setUsers(members);
            chat.setName(name);
            chat.save();

            chat.sendMessage(from, firstMsg);

            Ebean.commitTransaction();
        } catch (Exception e){
            throw new CreateMessageException("Die Nachricht konnte nicht erzeugt werden.");
        } finally {
            Ebean.endTransaction();
        }
        chat.refresh();
        return chat;
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

    public Timestamp getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Timestamp lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * Returns the number of unread messages.
     *
     * @return
     */
    public int getUnreadMessagesCountFor(User user) {
        return Message.find.where()
                .eq("chat", this)
                .eq("seen", false)
                .ne("user", user)
                .findRowCount();

    }

    /**
     * Returns a string with all members except of the given user.
     *
     * @param exceptUser
     * @return
     */
    public String getChatMembersExceptOf(User exceptUser) {
        List<User> members = getUsers();
        members.remove(exceptUser);
        String result = "";
        for (User user: members) {
            result += user;
        }
        return result;
    }

    /**
     * Sends a message to a chat by a user.
     *
     * @param from
     * @param msg
     * @throws CreateMessageException
     */
    public void sendMessage(User from, String msg) throws CreateMessageException {
        if (!getUsers().contains(from)) {
            throw new CreateMessageException("Der User ist kein Teilnehmer dieses Gespräches.");
        }

        // Send message
        Message message = new Message();
        message.setChat(this);
        message.setUser(from);
        message.setText(msg);
        message.setSeen(false);
        message.insert();

        // Update last message
        this.refresh();
        this.setLastMessage(Timestamp.valueOf(LocalDateTime.now()));
        this.update();
    }
}
