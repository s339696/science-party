package controllers;

import controllers.*;
import controllers.routes;
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

    public Result renderFriends(String feedback) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<User> users = User.find.all();
        users.remove(user);
        
        List<User> requestUsers = user.getFriendRequests();
        List<User> friendUsers = user.getFriends();

        return ok(views.html.friends.friends.render(users,requestUsers,friendUsers,feedback));
    }

    public Result addFriend(Long id) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        } else if (user.getId() == id) {
            return renderFriends("Du kannst dich nicht selber als Freund hinzufügen.");
        }

        User newFriend = User.find.byId(id);
        if (newFriend == null) {
            return renderFriends("Es gibt keinen User mit der Id #" + id + ".");
        }

        try {
            user.sendFriendRequestTo(newFriend);
        } catch (FriendRequestException e) {
            return renderFriends(e.getMessage());
        }

        return renderFriends("Die Anfrage wurde verschickt.");

    }

    public Result deleteFriend(Long id) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        User friend = User.find.byId(id);
        if (friend == null) {
            return renderFriends("Es gibt keinen User mit der Id #" + id + ".");
        }

        Friend friendship = user.getFriendshipWith(friend);
        if (friendship == null) {
            return renderFriends("Die beiden User sind keine Freunde.");
        }
        System.out.println("Friendship vorhanden.");
        friendship.delete();

        return renderFriends("Eure Freundschaft wurde beendet.");
    }

    public Result responseFriendRequest(Long id, Integer action) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        User friend = User.find.byId(id);
        if (friend == null) {
            return renderFriends("Es gibt keinen User mit der Id #" + id.toString() + ".");
        }

        try {
            user.responeFriendRequestFrom(friend, (action == 1 ? true : false));
        } catch (FriendRequestException e) {
            return renderFriends(e.getMessage());
        }
        return renderFriends("");
    }
}
