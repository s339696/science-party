package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Handles all requests related to the ranking.
 */
public class Ranking extends Controller {

    public Result renderRanking() {
        return ok(views.html.ranking.ranking.render("Ranking"));
    }
}
