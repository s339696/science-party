package controllers;

import controllers.*;
import manager.LoginManager;
import models.ebean.Game;
import models.ebean.Player;
import models.ebean.User;
import play.mvc.*;

import java.util.List;

import static play.mvc.Results.redirect;

/**
 * The publ class handles all requests related to the Useraccount.
 */
public class Profil extends Controller {

    public Result renderOwnProfile() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Application.renderHome());
        } else {
            return redirect(controllers.routes.Profil.renderProfile(user.getId()));
        }
    }

    public Result renderProfile(long id) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        return ok(views.html.profile.profile.render(User.find.byId(id)));
    }

    public Result handleProfileUpdate() {
        return ok();
    }

    public Result handleProfileDelete(Boolean confirmed) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        if (confirmed) {
            // Check games if user is active player and set it to the next one
            List<Game> runningGames = user.getRunningGames();
            for (Game game : runningGames) {
                Player player = game.getPlayerForUser(user);
                if (game.getActivePlayer().equals(player)) {
                    game.nextTurn();
                }
            }

            // Logout and delete Account
            LoginManager.logout();
            user.delete();
            return redirect(controllers.routes.Application.renderHome());
        } else {
            return ok(views.html.profile.deleteProfile.render(user));
        }
    }

    public Result handleLogout() {
        LoginManager.logout();
        return redirect(controllers.routes.Application.renderHome());
    }

    /**
     * Handle the scan of a qr code
     *
     * @param code
     * @return
     */
    public Result handleScanQr(String code) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }



        return ok();
    }
}
