package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Handles all requests related to the friendship function.
 */
public class Friends extends Controller {

    public Result renderFriends() {
        return ok(views.html.friends.friends.render("Freunde"));
    }
}
