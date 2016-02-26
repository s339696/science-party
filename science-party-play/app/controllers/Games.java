package controllers;

import controllers.*;
import manager.GameManager;
import manager.LoginManager;
import models.ebean.Game;
import models.ebean.Topic;
import models.ebean.User;
import models.form.CreateGameForm;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all request related to the gameplay.
 */
public class Games extends Controller {

    public Result renderGames() {
        return redirect(controllers.routes.Games.renderRunningGames());
    }

    public Result renderPendingGames() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        List<Game> pendingGames = GameManager.getPendingGames(user.getId());

        for (Game game: pendingGames) {
            Logger.info(game.getId().toString());
        }

        return ok("Pending Games");
    }

    public Result renderRunningGames() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        List<Game> runningGames = GameManager.getRunningGames(user.getId());

        for (Game game: runningGames) {
            Logger.info(game.getId().toString());
        }

        return ok("Running Games");
    }

    public Result renderGame(Long id) {
        return ok();
    }

    /**
     * Creates a new Game with the following JSON data.
     *
     * {
     * "topicId":"1",
     * "playerIds":[2,3,4]
     * }
     *
     * @return
     */
    public Result createGame() {
        User player1 = LoginManager.getLoggedInUser();
        if (player1 == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        Form<CreateGameForm> requestData = Form.form(CreateGameForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        }
        CreateGameForm cgForm = requestData.get();

        //Get Topic and create object
        int topicId = cgForm.getTopicId();
        Topic topic = Topic.find.byId((long)topicId);
        if (topic == null) {
            return badRequest("Das ausgewählte Thema ist ungültig.");
        }

        // Get involved Users and add them to Playerlist
        List<User> playerList = new ArrayList<User>();
        playerList.add(player1);

        int[] playerIds = cgForm.getPlayerIds();
        for (int i = 0; i < playerIds.length; i++) {
            User player = User.find.byId((long) playerIds[i]);
            if (player == null) {
                return badRequest("Spieler " + (i + 2) + " konnte dem Spiel nicht hinzugefügt werden.");
            } else {
                playerList.add(player);
            }
        }

        //Create Games
        Game game = GameManager.createGame(topic, playerList);
        if (game==null) {
            return badRequest("Das Spiel konnte nicht erzeugt werden.");
        }
        return ok("Das Spiel mit der Id #" + game.getId() + " wurde erfolgreich erzeugt.");
    }

    public Result deleteGame(Long id) {
        return ok();
    }

    public Result acceptGame(Long id) {
        return ok();
    }

    public Result declineGame(Long id) {
        return ok();
    }

    public Result leaveGame(Long id) {
        return ok();
    }

}
