package controllers;


import manager.LoginManager;
import models.ebean.User;

import play.mvc.*;
import play.Logger;

import views.html.*;

public class Application extends Controller {

    public Result renderHome() {
        return ok(views.html.home.render("Willkommen bei Science Party"));
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

    public Result playground() {

        return ok("Let's Play!!!");
    }

}
