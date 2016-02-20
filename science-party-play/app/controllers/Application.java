package controllers;

import models.ebean.User;
import play.*;
import play.mvc.*;

public class Application extends Controller {

    public Result index() {
        // Kurzer Datenbank Test
        System.out.println(User.find.byId(1L).username);

        return ok(views.html.index.render("Willkommen bei Science Party"));
    }

    public Result home() {
        return ok(views.html.home.render("Willkommen bei Science Party"));
    }

    public Result login() {
        return ok(views.html.login.render("Anmeldung"));
    }

    public Result register() {
        return ok(views.html.register.render("Registrierung"));
    }

    public Result blog() {
        return ok(views.html.blog.render("Blog"));
    }

    public Result events() {
        return ok(views.html.events.render("Events"));
    }

    public Result kontakt() {
        return ok(views.html.kontakt.render("Kontakt"));
    }

}
