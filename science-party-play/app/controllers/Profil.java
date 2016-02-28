package controllers;

import manager.LoginManager;
import models.ebean.User;
import play.mvc.*;

import static play.mvc.Results.redirect;

/**
 * The public class handles all requests related to the Useraccount.
 */
public class Profil extends Controller {

    public Result renderOwnProfil() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Application.renderHome());
        } else {
            return redirect(controllers.routes.Profil.renderProfil(user.getId()));
        }
    }

    public Result renderProfil(long id) {

        return ok(views.html.profile.render(User.find.byId(1L)));
    }

    public Result handleProfileUpdate() {
        return ok();
    }

    public Result handleProfileDelete() {
        return ok();
    }

    public Result handleLogout() {
        LoginManager.logout();
        return redirect(controllers.routes.Application.renderHome());
    }
}
