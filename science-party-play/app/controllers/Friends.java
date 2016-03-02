package controllers;

import manager.LoginManager;
import models.ebean.Friend;
import models.ebean.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Handles all requests related to the friendship function.
 */
public class Friends extends Controller {

    public Result renderFriends() {
        List<User> users = User.find.all();

        return ok(views.html.friends.friends.render(users,users,users));
    }

    public Result addFriend(Long id) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        } else if (user.getId() == id) {
            return badRequest("Du kannst dich nicht selber als Freund hinzufügen.");
        }

        User newFriend = User.find.byId(id);
        if (newFriend == null) {
            return badRequest("Es gibt keinen User mit der Id #" + id);
        }

        user.sendFriendRequest(newFriend);

        return ok("Die Anfrage wurde verschickt.");

    }

    public Result deleteFriend(Long id) {

        return ok();
    }

    public Result acceptFriend(Long id) {
        return ok();
    }

    public Result declineFriend(Long id) {
        return ok();
    }
}
