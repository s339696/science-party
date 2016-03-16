package controllers;

import com.avaje.ebean.Ebean;
import exception.messages.CreateMessageException;
import manager.LoginManager;
import models.ebean.Chat;
import models.ebean.Message;
import models.ebean.Notification;
import models.ebean.User;
import models.form.MessageForm;
import models.form.UserAccountForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all requests related to the messenger.
 */
public class Messages extends Controller {

    /**
     * Renders all messages of a chat.
     *
     * @return
     */
    public Result renderMessages() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<Chat> chats = user.getChats();

        return ok(views.html.messages.messages.render(user, chats));
    }

    /**
     * Renders the page to create a new chat.
     *
     * @return
     */
    public Result renderNewMessage() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<User> users = User.find.all();
        users.remove(user);

        return ok(views.html.messages.newMessage.render(user, users));
    }

    /**
     * Creates a new chat between 2 or more users.
     *
     * @return
     */
    public Result handleNewMessage() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        // Get data from request
        Form<MessageForm> requestData = Form.form(MessageForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        } else {
            MessageForm form = requestData.get();
            try {
                // Generate list of chat members
                int[] memberIds = form.getToIds();
                List<User> members = new ArrayList<User>();
                Ebean.beginTransaction();
                try {
                    if (memberIds.length < 1) {
                        badRequest("Es wurde kein Gesprächsteilnehmer angegeben.");
                    }
                    for (int i = 0; i < memberIds.length; i++) {
                        User member = User.find.byId((long) memberIds[i]);
                        members.add(member);
                    }
                    Ebean.commitTransaction();
                } catch (Exception e) {
                    badRequest("Die angegebenen Gesprächsteilnehmer sind nicht konsistent.");
                } finally {
                    Ebean.endTransaction();
                }

                String name = "";
                for (User chatUser : members) {
                    name += chatUser.getFirstname();
                    name += ", ";
                }
                name += user.getFirstname();

                // Create chat
                Chat.createChat(user, members, name, form.getMessage());
                return ok("Die Nachricht wurde erfolgreich verschickt.");

            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
        }
    }

    /**
     * Is called to leave a chat.
     *
     * @param chatId
     * @return
     */
    public Result handleLeaveChat(Long chatId) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        // Get chat
        Chat chat = Chat.find.byId(chatId);
        if (chat == null) {
            return badRequest("Es gibt kein Gespräch mit der Id #" + chatId + ".");
        }

        chat.getUsers().remove(user);
        chat.update();

        return ok("Das Gespräch wurde verlassen.");
    }

    /**
     * Renders the page to show a chat.
     *
     * @param chatId
     * @return
     */
    public Result renderShowChat(Long chatId) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        Chat chat = Chat.find.byId(chatId);
        if (chat == null) {
            return badRequest("Es gibt kein Gespräch mit der Id #" + chatId + ".");
        }

        List<Message> messages = Message.getMessagesOfChat(chat);

        Ebean.beginTransaction();
        try {
            for (Message message : messages) {
                if (!message.getUser().equals(this)) {
                    message.setSeen(true);
                    message.save();
                }
            }
            Ebean.commitTransaction();
        } finally {
            Ebean.endTransaction();
        }
        
        return ok(views.html.messages.viewMessage.render(user, messages, chat));
    }

    /**
     * Handle the request to send a message.
     *
     * @param chatId
     * @return
     */
    public Result handleSendMessage(Long chatId) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        // Get chat
        Chat chat = Chat.find.byId(chatId);
        if (chat == null) {
            return badRequest("Es gibt kein Gespräch mit der Id #" + chatId + ".");
        }

        // Get data from request
        Form<MessageForm> requestData = Form.form(MessageForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        }
        MessageForm form = requestData.get();

        // Send message
        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setText(form.getMessage());
        message.setSeen(false);
        message.insert();

        return ok("Die Nachricht wurde verschickt.");
    }

    /**
     * Returns the amount of unseen messages.
     *
     * @return
     */
    public Result handleNewMessageCount() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("");
        }

        int count = user.getUnseenMessageCount();

        return ok(String.valueOf(count));
    }

}
