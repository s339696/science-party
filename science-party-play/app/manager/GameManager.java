package manager;

import exception.games.GameException;
import exception.games.StartGameException;
import exception.games.StopGameException;
import models.ebean.*;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The GameManger manages the whole Gameplay of a Match.
 */
public class GameManager {

    /**
     * Creates a new Game object and the required database inserts.
     *
     * @param topic
     * @param users
     * @return
     */
    public static Game createGame(Topic topic, List<User> users) throws Exception {
        // TODO: Nullpointercheck for topic

        // Create actual game
        Game game = new Game();
        game.setGameStatus(Game.GameStatus.PENDING);
        game.setTopic(topic);

        game.insert();

        // Create Players for the Games, each user represent a player
        if (users.size() < 1 || users.size() > 4) {
            //TODO: Invalid amount of players
        }
        List<Player> players = new ArrayList<Player>();
        for (User user : users) {
            Player player = new Player();
            player.setFieldPosition(0);
            player.setPlayerStatus(Player.PlayerStatus.INVITED);
            player.setUser(user);
            player.setGame(game);

            //If user is host, set PlayStatus to Accepted
            if (LoginManager.getLoggedInUser().getId() == user.getId()) {
                player.setPlayerStatus(Player.PlayerStatus.ACCEPTED);
            }

            player.insert();
        }

        //Try to start game directly if it is a one player game
        game.refresh();
        startGame(game);

        return game;
    }

    /**
     * Accept the invitation and try to start the game
     *
     * @param game
     * @param user
     * @throws Exception
     */
    public static void respondGameInvite(Game game, User user, boolean accept) throws Exception {
        Player player = Player.getPlayerOfGameAndUser(game, user);

        // Accept if player found
        if (player == null || player.getPlayerStatus() != Player.PlayerStatus.INVITED) {
            throw new GameException("Der angegebene User wurde nicht zu diesem Spiel eingeladen oder hat die Anfrage bereits beantwortet.");
        } else {
            if (accept) {
                player.setPlayerStatus(Player.PlayerStatus.ACCEPTED);
            } else {
                player.setPlayerStatus(Player.PlayerStatus.DECLINE);
            }
            player.update();
        }

        // Try to start the game
        startGame(game);
    }

    /**
     * Leave a running game
     *
     * @param game
     * @param user
     */
    public static void leaveGame(Game game, User user) throws GameException {
        // Leave game
        Player player = Player.getPlayerOfGameAndUser(game, user);

        if (player == null || player.getPlayerStatus() != Player.PlayerStatus.PLAYING) {
            throw new GameException("Der angegebene User ist kein aktives Mitglied dieses Spieles.");
        }

        if (player.getPlayerStatus() == Player.PlayerStatus.PLAYING) {
            player.setPlayerStatus(Player.PlayerStatus.LEFT);
        }
        player.update();

        // Check if there are still people who play, otherwise stop!
        if (!game.hasPlayingPlayer()) {
            if (stopGame(game)) {
                throw new StopGameException("Das Spiel wurde verlassen und beendet.");
            }
        }
    }

    /**
     * Starts a game if it is possible.
     *
     * @param game
     * @throws StartGameException
     */
    public static void startGame(Game game) throws StartGameException {
        List<Player> players = game.getPlayers();

        // Check if game is already running or finished
        if (game.getGameStatus() != Game.GameStatus.PENDING) {
            throw new StartGameException("Das Spiel wurde bereits gestartet.", game);
        }

        // Check if all player responded the invitation
        for (Player player : players) {
            if (player.getPlayerStatus() != Player.PlayerStatus.ACCEPTED &&
                    player.getPlayerStatus() != Player.PlayerStatus.DECLINE) {
                throw new StartGameException("Das Spiel konnte nicht gestartet werden, da noch nicht alle Spieler ihre Einladung beantwortet haben.", game);
            }
        }

        // All players responded, let's set them to PLAYING:
        for (Player player : players) {
            if (player.getPlayerStatus() == Player.PlayerStatus.ACCEPTED) {
                player.setPlayerStatus(Player.PlayerStatus.PLAYING);
            }
            player.update();
        }

        // Random start: Generate starting player id as long a starting player has status PLAYING
        Random random = new Random();
        int startNumb;
        do {
            startNumb = random.nextInt(players.size());
        } while (Player.find.byId((long) startNumb).getPlayerStatus() != Player.PlayerStatus.PLAYING);

        // Set starting player, starting question and start game
        game.setActivePlayer(players.get(startNumb));
        game.setActiveQuestion(Question.getRandomQuestion(game.getTopic()));
        game.setGameStatus(Game.GameStatus.ACTIVE);
        game.update();
    }

    /**
     * Stops a game independent to its state
     *
     * @param game
     */
    public static boolean stopGame(Game game) throws GameException {
        List<Player> players = game.getPlayers();

        // Set game and player to state FINISHED
        if (game.getGameStatus() == Game.GameStatus.ACTIVE) {

            game.setGameStatus(Game.GameStatus.FINISHED);
            game.setActivePlayer(null);
            game.setActiveQuestion(null);
            game.update();

            for (Player player : players) {
                if (player.getPlayerStatus() == Player.PlayerStatus.PLAYING) {
                    // Change player status for playing players
                    player.setPlayerStatus(Player.PlayerStatus.FINISHED);
                    player.update();
                    // Update global ranking
                    User userOfPlayer = player.getUser();
                    userOfPlayer.setPoints(userOfPlayer.getPoints() + player.getFieldPosition());
                    userOfPlayer.update();
                }


            }
        } else {
            throw new GameException("Das Spiel kann nicht gestoppt werden.");
        }

        return true;
    }

    /**
     * Answer the Question and process the turn
     *
     * @param game
     * @param answer
     */
    public static boolean answerActiveQuestion(Game game, Answer answer) throws GameException {
        boolean result = false;
        Answer correctAnswer = game.getActiveQuestion().getCorrectAnswer();
        Player activePlayer = game.getActivePlayer();

        if (correctAnswer == null) {
            throw new GameException("Es ist ein Fehler aufgetreten. Auf diese Frage gibt es leider keine korrekte Antwort.");
        }

        if (game.getGameStatus() != Game.GameStatus.ACTIVE) {
            throw new GameException("Es handelt sich um kein aktives Spiel.");
        }

        // Check if answer is correct.
        if (correctAnswer.getId() == answer.getId()) {
            // Step forward and update points of active player
            Question activeQuestion = game.getActiveQuestion();
            activePlayer.setFieldPosition(activePlayer.getFieldPosition() + activeQuestion.getDifficulty());
            activePlayer.update();

            result = true;
        }

        // Check if active player reached 40points
        if (activePlayer.getFieldPosition() >= 40) {
            // Set points to 40 to avoid more then 40 points per game and a invalid map position
            activePlayer.setFieldPosition(40);
            activePlayer.update();
            if (stopGame(game)) {
                throw new StopGameException("Du hast das Ziel erreicht und das Spiel gewonnen.");
            }
        }

        // Pass turn to next player
        game.nextTurn();

        return result;
    }
}
