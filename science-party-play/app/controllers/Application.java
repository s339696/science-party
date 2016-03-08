package controllers;


import exception.games.StartGameException;
import manager.GameManager;
import manager.LoginManager;
import manager.PerkManager;
import models.ebean.Game;
import models.ebean.Question;
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
            return redirect(controllers.routes.Profil.renderOwnProfile());
        } else {
            return ok(views.html.home.render("Willkommen bei Science Party"));
        }
    }

    /**
     * Time to play a bit and this doesn't mean to play a game...
     * --> Development in process.
     *
     * @return
     */
    public Result playground() {

        //Topic.find.byId(5L).delete();
        PerkManager.updatePerksPerTopic();

        return ok("Play!");
    }

}
