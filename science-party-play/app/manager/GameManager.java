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
        for (User user: users) {
            Player player = new Player();
            player.setFieldPosition(0);
            player.setPlayerStatus(Player.PlayerStatus.INVITED);
            player.setUser(user);
            player.setGame(game);

            player.insert();
        }

        return game;
    }
}
