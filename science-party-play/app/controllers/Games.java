package controllers;

import controllers.*;
import exception.games.GameException;
import exception.games.StartGameException;
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

    /**
     * Renders the start page of the games section.
     *
     * @return
     */
    public Result renderGames() {
        return redirect(controllers.routes.Games.renderRunningGames());
    }

    /**
     * Renders a list of games where the logged in user is part of and which has not started yet.
     *
     * @return
     */
    public Result renderPendingGames() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        List<Game> pendingGames = GameManager.getPendingGames(user.getId());

        for (Game game : pendingGames) {
            Logger.info(game.getId().toString());
        }

        return ok("Pending Games");
    }

    /**
     * Renders a list of all games where the logged in user is part of.
     *
     * @return
     */
    public Result renderRunningGames() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        List<Game> runningGames = GameManager.getRunningGames(user.getId());

        for (Game game : runningGames) {
            Logger.info(game.getId().toString());
        }

        return ok("Running Games");
    }

    /**
     * Renders a game with the given id if the user is part of the game.
     *
     * @param id
     * @return
     */
    public Result renderGame(Long id) {
        return ok();
    }

    /**
     * Creates a new Game with the following JSON data.
     * <p>
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
        Topic topic = Topic.find.byId((long) topicId);
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

        //Create Game
        Game game = null;
        try {
            game = GameManager.createGame(topic, playerList);
        } catch (StartGameException e) {
            return ok("Das Spiel mit der Id #" + e.getGame().getId() + " wurde erfolgreich erzeugt, " +
                    "konnte aber noch nicht gestartet werden.");
        } catch (Exception e) {
            // TODO:
        }
        if (game == null) {
            return badRequest("Das Spiel konnte nicht erzeugt werden.");
        }
        return ok("Das Spiel mit der Id #" + game.getId() + " wurde erfolgreich erzeugt und gestartet.");
    }

    public Result deleteGame(Long id) {
        return ok();
    }

    public Result respondGameInv(Long id, Integer action) {
        Game game = GameManager.getGameById(id);
        User user = LoginManager.getLoggedInUser();

        Boolean accept = (action == 1) ? true : false;

        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        if (game == null) {
            return badRequest("Es gibt kein Spiel mit der angegebenen Id #" + id + ".");
        }

        try {
            GameManager.respondGameInvite(game, user, accept);
        } catch (StartGameException e) {
            return ok(e.getMessage());
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        return ok("Die Einladung wurde angenommen und das Spiel gestartet.");
    }

    public Result leaveGame(Long id) {
        return ok();
    }

}
