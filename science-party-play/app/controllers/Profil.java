package controllers;

import controllers.*;
import exception.perks.GetPerkException;
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

    /**
     * Renders the own profile
     *
     * @return
     */
    public Result renderOwnProfile() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Application.renderHome());
        } else {
            return redirect(controllers.routes.Profil.renderProfile(user.getId()));
        }
    }

    /**
     * Renders the profile with the given id
     *
     * @param id
     * @return
     */
    public Result renderProfile(long id) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        boolean ownProfile = false;
        if (user.getId() == id) {
            ownProfile = true;
        }

        return ok(views.html.profile.profile.render(User.find.byId(id), ownProfile));
    }

    /**
     * TODO: Implementation und documentation
     *
     * @return
     */
    public Result handleProfileUpdate() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        return ok();
    }

    /**
     * Handle the request to delete a profile
     *
     * @param confirmed
     * @return
     */
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
     * Renders the page to scan a QR code
     *
     * @return
     */
    public Result renderScanQr() {
        return ok();
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

        try {
            user.addPerkFromQr(code);
        } catch (GetPerkException e) {
            badRequest(e.getMessage());
        }
        return ok("Die Fähigkeit wurde erfolgreich hinzugefügt und kann jetzt verwendet werden.");
    }
}
