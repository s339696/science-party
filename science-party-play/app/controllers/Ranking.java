package controllers;

import manager.LoginManager;
import models.ebean.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Handles all requests related to the ranking.
 */
public class Ranking extends Controller {

    public Result renderRanking() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        // Get list with user orderd by points
        List<User> users = User.find.orderBy().desc("points").setMaxRows(15).findList();
        return ok(views.html.ranking.ranking.render(user, users));
    }
}
