package controllers;

import com.avaje.ebean.Ebean;
import exception.messages.CreateMessageException;
import manager.LoginManager;
import models.ebean.Chat;
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

    public Result renderMessages() {

        return ok(views.html.messages.messages.render("Nachrichten"));
    }

    public Result renderNewMessage() {

        return ok(views.html.messages.newMessage.render());
    }

    public Result handleNewMessage() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
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
                        User member = User.find.byId((long)memberIds[i]);
                        members.add(member);
                    }
                    Ebean.commitTransaction();
                } catch (Exception e) {
                    badRequest("Die angegebenen Gesprächsteilnehmer sind nicht konsistent.");
                } finally {
                    Ebean.endTransaction();
                }

                // Create chat
                Chat.createChat(user, members, form.getMessage());
                return ok("Die Nachricht wurde erfolgreich verschickt.");

            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
        }
    }

    public Result handleLeaveChat(Long chatid) {

        return ok();
    }

    public Result renderShowChat(Long chatid) {

        return ok(views.html.messages.viewMessage.render());
    }

    public Result handleSendMessage(Long chatid) {
        return ok();
    }

}
