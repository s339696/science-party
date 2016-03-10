package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Handles all requests related to the messager.
 */
public class Messages extends Controller {

    public Result renderMessages() {
        return ok(views.html.messages.messages.render("Nachrichten"));
    }

}
