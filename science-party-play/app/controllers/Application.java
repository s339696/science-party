package controllers;

import models.ebean.User;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        // Kurzer Datenbank Test
        System.out.println(User.find.byId(1L).username);

        return ok(index.render("Your new application is ready."));
    }

}
