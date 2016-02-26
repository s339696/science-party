package controllers;


import manager.GameManager;
import manager.LoginManager;
import models.ebean.Game;
import models.ebean.Topic;
import models.ebean.User;
import play.mvc.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle general Application requests.
 * @return
 */
public class Application extends Controller {

    /**
     * This methods renders the one and only index/root/home page if no user is logged in. Otherwise the User will
     * redirected to his own Profilpage.
     * @return
     */
    public Result renderHome() {
        if (LoginManager.isLoggedIn()) {
            return redirect(controllers.routes.Profil.renderOwnProfil());
        } else {
            return ok(views.html.home.render("Willkommen bei Science Party"));
        }
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

    /**
     * Time to play a bit and this doesn't mean to play a game...
     * --> Development in process.
     *
     * @return
     */
    public Result playground() {
        Topic topic = Topic.find.byId(1L);

        User user1 = User.find.byId(1L);
        User user2 = User.find.byId(2L);

        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        Game game = GameManager.createGame(topic, users);

        return ok("Let's Play!!!");
    }

}
