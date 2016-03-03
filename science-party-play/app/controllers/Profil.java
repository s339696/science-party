package controllers;

import controllers.*;
import manager.LoginManager;
import models.ebean.User;
import play.mvc.*;

import static play.mvc.Results.redirect;

/**
 * The publ class handles all requests related to the Useraccount.
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
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        return ok(views.html.profile.profile.render(User.find.byId(id)));
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
