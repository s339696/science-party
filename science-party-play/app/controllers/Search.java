package controllers;

import manager.LoginManager;
import models.ebean.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Implements a live search for various objects.
 */
public class Search extends Controller {

    /**
     * Returns the proper formatted search result of a user search for a friends request.
     *
     * @param sstring
     * @return
     */
    public Result searchUserForNewFriends(String sstring) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<User> resultUsers = searchUser(sstring);
        resultUsers.remove(user);
        resultUsers.removeAll(user.getFriends());
        resultUsers.removeAll(user.getFriendRequests());

        return ok(views.html.search.userNewFriendSearch.render(resultUsers));
    }

    /**
     * Returns a list of users which are fitting to the search string.
     *
     * @param sstring
     * @return
     */
    public List<User> searchUser(String sstring) {
        String[] sstrings = sstring.split(" ");
        if (sstrings.length > 0) {
            sstring = "";
            for (int i = 0; i < sstrings.length; i++) {
                sstrings[i] += "* ";
                sstring += sstrings[i];
            }
        } else {
            sstring += "*";
        }

        String query = "WHERE MATCH (firstname,lastname,email) AGAINST ('" + sstring + "' IN BOOLEAN MODE)";
        List<User> resultUsers = User.find
                //.where().contains("firstname", sstring)
                .setQuery(query)
                .findList();

        return resultUsers;
    }

/*    public List<User> searchUser(String sstring) {
        //String query = "WHERE MATCH (firstname,lastname,email) AGAINST ('" + sstring + "' IN BOOLEAN MODE)";
        List<User> resultUsers = User.find
                .where().contains("firstname", sstring)
                //.setQuery(query)
                .findList();

        return resultUsers;
    }*/

}


