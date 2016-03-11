package controllers;

import controllers.*;
import controllers.routes;
import exception.games.GameException;
import exception.games.StartGameException;
import exception.games.StopGameException;
import manager.GameManager;
import manager.LoginManager;
import models.ebean.*;
import models.form.AnswerForm;
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
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        return ok(views.html.games.mainGame.render("Games"));
    }

    /**
     * Renders the page to create a new game.
     */
    public Result renderCreateGame() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        return ok(views.html.games.createGame.render(Topic.find.all(), User.find.all()));
    }

    /**
     * Renders a list of games where the logged in user is part of and which has not started yet.
     *
     * @return
     */
    public Result renderPendingGames(String feedback) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        return ok(views.html.games.pendingGames.render(user, feedback));
    }

    /**
     * Renders a list of all games where the logged in user is part of.
     *
     * @return
     */
    public Result renderRunningGames(String feedback) {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<Game> runningGames = user.getRunningGames();

        return ok(views.html.games.loadGame.render(runningGames, feedback));
    }

    /**
     * Renders a game with the given id if the user is part of the game.
     *
     * @param id
     * @return
     */
    public Result renderGame(Long id) {
        Game game = Game.getGameById(id);
        User user = LoginManager.getLoggedInUser();

        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        if (game == null) {
            return renderRunningGames("Es gibt kein Spiel mit der angegebenen Id #" + id + ".");
        }

        Player player = Player.getPlayerOfGameAndUser(game, user);
        System.out.println(player.getPlayerStatus());
        if (player == null || player.getPlayerStatus() != Player.PlayerStatus.PLAYING) {
            return renderRunningGames("Du bist kein aktiver Mitspieler dieses Spiels.");
        }

        return ok(views.html.games.playGame.render(player, game));
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
    public Result handleCreateGame() {
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
            return badRequest(e.getMessage());
        }
        if (game == null) {
            return badRequest("Das Spiel konnte nicht erzeugt werden.");
        }
        return ok("Das Spiel mit der Id #" + game.getId() + " wurde erfolgreich erzeugt und gestartet.");
    }

    public Result deleteGame(Long id) {
        return ok();
    }

    /**
     * Handle the response to a game invitation.
     * action = 1 --> accept invite
     * action = 0 --> decline invite
     *
     * @param id
     * @param action
     * @return
     */
    public Result respondGameInv(Long id, Integer action) {
        Game game = Game.getGameById(id);
        User user = LoginManager.getLoggedInUser();

        Boolean accept = (action == 1) ? true : false;

        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        if (game == null) {
            return renderPendingGames("Es gibt kein Spiel mit der angegebenen Id #" + id + ".");
        }

        try {
            GameManager.respondGameInvite(game, user, accept);
        } catch (StartGameException e) {
            return renderPendingGames(e.getMessage());
        } catch (Exception e) {
            return renderPendingGames("Fehler: " + e.getMessage());
        }

        return redirect(controllers.routes.Games.renderGame(game.getId()));
    }

    /**
     * Handles the request to leave a game.
     *
     * @param id
     * @return
     */
    public Result leaveGame(Long id) {
        Game game = Game.getGameById(id);
        User user = LoginManager.getLoggedInUser();

        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }

        if (game == null) {
            return badRequest("Es gibt kein Spiel mit der angegebenen Id #" + id + ".");
        }

        try {
            GameManager.leaveGame(game, user);
        } catch (GameException e) {
            return badRequest(e.getMessage());
        }
        return ok("Das Spiel wurde verlassen.");
    }

    /**
     * Handles the response to the active question of the given game.
     * <p>
     * {
     * "answerId":"1",
     * }
     *
     * @param id
     * @return
     */
    public Result handleAnswer(Long id) {
        // Check game and user
        Game game = Game.getGameById(id);
        //User user = LoginManager.getLoggedInUser();

/*        if (user == null) {
            return badRequest("Es ist kein User eingeloggt.");
        }*/

        if (game == null) {
            return badRequest("Es gibt kein Spiel mit der angegebenen Id #" + id + ".");
        }

        // Parse answer
        Form<AnswerForm> requestData = Form.form(AnswerForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        }

        AnswerForm answerForm = requestData.get();
        Answer answer = Answer.find.byId((long) answerForm.getAnswerId());

        // Give the answer to the game god and let him do

        try {
            if (GameManager.answerActiveQuestion(game, answer)) {
                return ok("Die Frage wurde richtig beantwortet. Der nächste Spieler ist jetzt an der Reihe.");
            } else {
                return ok("Die Frage wurde falsch beantwortet. Der nächste Spieler ist jetzt an der Reihe.");
            }
        } catch (StopGameException e) {
            return ok(e.getMessage());
        } catch (GameException e) {
            return badRequest(e.getMessage());
        }
    }

}
