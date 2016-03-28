package controllers;

import exception.perks.GetPerkException;
import manager.LoginManager;
import models.ebean.Game;
import models.ebean.Player;
import models.ebean.User;
import models.form.UserAccountForm;
import play.data.Form;
import play.mvc.*;
import util.Helper;

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
    public Result renderProfile(long id, String feedback) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        boolean ownProfile = false;
        if (user.getId() == id) {
            ownProfile = true;
        }

        return ok(views.html.profile.profile.render(User.find.byId(id), ownProfile, feedback));
    }

    /**
     * Handles the update of a profile.
     * If the password is net changed, the password field can be missing ore "".
     * Required data:
     * {
     * "firstname": "Erik",
     * "lastname": "Wolf",
     * "email": "erik.wolf.29@gmail.com",
     * "password": "1234",
     * "birthday": "29.10.1986"
     * }
     *
     * @return
     */
    public Result handleProfileUpdate() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        Form<UserAccountForm> requestData = Form.form(UserAccountForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        } else {
            UserAccountForm regForm = requestData.get();
            try {
                user.setFirstname(regForm.getFirstname());
                user.setLastname(regForm.getLastname());
                user.setBirthdayByString(regForm.getBirthday());
                user.update();
                if (regForm.getPassword() != null && !regForm.getPassword().equals("") && !Helper.getMD5fromString(regForm.getPassword()).equals(user.getPassword())) {
                    user.setPassword(Helper.getMD5fromString(regForm.getPassword()));
                    user.update();
                    LoginManager.logout();
                    return ok("Das Profil wurde erfolgreich geändert. Da das Passwort geändert wurde, musst du dich neu anmelden.");
                }
                return ok("Das Profil wurde erfolgreich geändert.");
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
        }
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

    /**
     * Proceed a logout for the current user.
     *
     * @return
     */
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
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        return ok(views.html.profile.qrScanner.render(user));
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
            return renderProfile(user.getId(), e.getMessage());
        }
        return renderProfile(user.getId(), "Die Fähigkeit wurde erfolgreich hinzugefügt und kann jetzt verwendet werden.");
    }
}
