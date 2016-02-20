package controllers;

import com.avaje.ebean.Ebean;
import manager.LoginManager;
import models.ebean.Perk;
import models.ebean.Topic;
import models.ebean.User;
import play.*;
import play.mvc.*;
import play.Logger;

import util.Helper;
import views.html.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Application extends Controller {

    public Result index() {
        // Kurzer Datenbank Test

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

    public Result playground() {

        User owner = LoginManager.login("erik.wolf.29@gmail.com","1234");
        if (owner != null) {
            Logger.info("Login: " + owner.toString());
        }


        owner = null;
        owner = LoginManager.getLoggedInUser();
        if (owner != null) {
            Logger.info("Get User: " + owner.toString());
        }

        Logger.info(String.valueOf(LoginManager.isLoggedIn()));

        LoginManager.logout();

        Logger.info(String.valueOf(LoginManager.isLoggedIn()));

/*        User user = User.find.byId(1L);

        Topic topic = new Topic();
        topic.setName("Forschungsmethoden");
        topic.insert();

        if (user == null) {
            user = new User();
            user.setFirstname("Erik");
            user.setLastname("Wolf");
            Ebean.save(user);
        }

        Perk perk = Perk.find.byId(1L);

        if (perk == null) {
            perk = new Perk();
            perk.setPerkName("Aussetzen");
            perk.setQrCode(Helper.getMD5fromString("Aussetzen"));
            Ebean.save(perk);
        }

        List<Perk> perks = user.getPerks();
        if (!perks.contains(perk)) {
            user.getPerks().add(perk);
        }

        user.update();*/

        //user.update();
        // Logger.info(user.firstname + " " + user.lastname + " " + user.email + " " + user.birthday.toLocalDateTime().toString());

        //Ebean.delete(user);
        //user.delete();
        return ok(playground.render("Let's Play!"));

    }

}
