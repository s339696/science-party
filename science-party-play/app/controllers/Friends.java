package controllers;

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

        return ok(views.html.friends.friends.render(users));
    }
}
