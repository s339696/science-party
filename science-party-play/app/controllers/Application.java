package controllers;

import models.ebean.User;
import play.*;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        // Kurzer Datenbank Test
        System.out.println(User.find.byId(1L).username);

        return ok(index.render("Willkommen bei Science Party"));
    }

    public Result home() {
        return ok(home.render("Willkommen bei Science Party"));
    }

    public Result login() {
        return ok(login.render("Anmeldung"));
    }

    public Result register() {
        return ok(register.render("Registrierung"));
    }

    public Result blog() {
        return ok(blog.render("Blog"));
    }

    public Result events() {
        return ok(events.render("Events"));
    }

    public Result kontakt() {
        return ok(kontakt.render("Kontakt"));
    }

}
