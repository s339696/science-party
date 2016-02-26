package manager;

import com.avaje.ebean.Ebean;
import models.ebean.Game;
import models.ebean.Player;
import models.ebean.Topic;
import models.ebean.User;

import java.util.ArrayList;
import java.util.List;

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
    public static Game createGame(Topic topic, List<User> users) {
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

            player.insert();
        }

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
                .ilike("game_status", "P")
                .eq("user_id", userId)
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
                .findList();

        return runningGames;
    }
}
