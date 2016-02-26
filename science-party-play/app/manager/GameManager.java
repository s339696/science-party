package manager;

import exception.games.StartGameException;
import models.ebean.Game;
import models.ebean.Player;
import models.ebean.Topic;
import models.ebean.User;
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
        game.setActivePlayer(0);
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
     * Returns a list with all pending games for a given user id.
     *
     * @param userId
     * @return
     */
    public static List<Game> getPendingGames(Long userId) {
        List<Game> pendingGames = Game.find
                .fetch("players")
                .where()
                .ieq("game_status", "P")
                .eq("user_id", userId)
                .ne("player_status", "L")
                .findList();

        return pendingGames;
    }

    /**
     * Returns a list with all running games for a given user id.
     *
     * @param userId
     * @return
     */
    public static List<Game> getRunningGames(Long userId) {
        List<Game> runningGames = Game.find
                .fetch("players")
                .where()
                .ilike("game_status", "A")
                .eq("user_id", userId)
                .ne("player_status", "L")
                .findList();

        return runningGames;
    }

    /**
     * Starts a game if it is possible.
     * @param game
     * @throws StartGameException
     */
    public static void startGame(Game game) throws StartGameException {
        List<Player> players = game.getPlayers();

        // Check if game is already running or finished
        if (game.getGameStatus() != Game.GameStatus.PENDING) {
            throw new StartGameException("Das Spiel wurde bereits gestartet.", game);
        }

        // Check if all player accepted the invitation
        for (Player player: players) {
            Logger.info(player.getUser().getEmail());
            if (player.getPlayerStatus() != Player.PlayerStatus.ACCEPTED) {
                throw new StartGameException("Es haben noch nicht alle Spieler ihre Einladung angenommen.", game);
            }
        }

        // All players accepted, let's set them to PLAYING:
        for (Player player: players) {
            player.setPlayerStatus(Player.PlayerStatus.PLAYING);
            player.update();
        }

        // Set activePlayer to random
        Random random = new Random();
        int startNumb = random.nextInt(players.size());
        game.setActivePlayer(players.get(startNumb).getId().intValue());
        game.setGameStatus(Game.GameStatus.ACTIVE);
        game.update();
    }
}
