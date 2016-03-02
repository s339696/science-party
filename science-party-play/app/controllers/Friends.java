package controllers;

import exception.friends.FriendRequestException;
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
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<User> users = User.find.all();
        List<User> requestUsers = user.getFriendRequests();
        List<User> friendUsers = user.getFriends();

        return ok(views.html.friends.friends.render(users,requestUsers,friendUsers));
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

        try {
            user.sendFriendRequestTo(newFriend);
        } catch (FriendRequestException e) {
            return badRequest(e.getMessage());
        }

        return ok("Die Anfrage wurde verschickt.");

    }

    public Result deleteFriend(Long id) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        User friend = User.find.byId(id);
        if (friend == null) {
            return badRequest("Es gibt keinen User mit der Id #" + friend.getId() + ".");
        }

        Friend friendship = user.getFriendshipWith(friend);
        if (friendship == null) {
            return badRequest("Die beiden User sind keine Freunde.");
        }

        friendship.delete();

        return ok("Eure Freundschaft wurde beendet.");
    }

    public Result responseFriendRequest(Long id, Integer action) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        User friend = User.find.byId(id);
        if (friend == null) {
            return badRequest("Es gibt keinen User mit der Id #" + id.toString() + ".");
        }

        try {
            user.responeFriendRequestFrom(friend, (action == 1 ? true : false));
        } catch (FriendRequestException e) {
            return ok(e.getMessage());
        }
        return ok();
    }
}
