package controllers;

import models.ebean.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Implements a live search for various objects.
 */
public class Search extends Controller {

    public Result searchUserForNewFriends(String sstring) {
        return ok(views.html.search.userNewFriendSearch.render(searchUser(sstring)));
    }

    public List<User> searchUser(String sstring) {
        //String query = "WHERE MATCH (firstname,lastname,email) AGAINST ('" + sstring + "' IN BOOLEAN MODE)";
        List<User> resultUsers = User.find
                .where().contains("firstname", sstring)
                //.setQuery(query)
                .findList();

        return resultUsers;
    }

}


