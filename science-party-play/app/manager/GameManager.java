package manager;

import exception.games.GameException;
import exception.games.StartGameException;
import exception.games.StopGameException;
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
     * Accept the invitation and try to start the game
     *
     * @param game
     * @param user
     * @throws Exception
     */
    public static void respondGameInvite(Game game, User user, boolean accept) throws Exception {
        Player player = getPlayerOfGameAndUser(game, user);

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
        Player player = getPlayerOfGameAndUser(game, user);

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

        // Set starting player and start game
        game.setActivePlayer(players.get(startNumb).getId().intValue());
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
            game.setActivePlayer(0);
            game.update();

            for (Player player : players) {
                if (player.getPlayerStatus() == Player.PlayerStatus.PLAYING) {
                    player.setPlayerStatus(Player.PlayerStatus.FINISHED);
                    player.update();
                }
            }
        } else {
            throw new GameException("Das Spiel kann nicht gestoppt werden.");
        }

        //TODO: Maybe update ranking
        return true;
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
     * Return the player object that matches the given game and user.
     *
     * @param game
     * @param user
     * @return
     */
    public static Player getPlayerOfGameAndUser(Game game, User user) {
        return Player.find.where()
                .eq("user_id", user.getId())
                .eq("game_id", game.getId())
                .findUnique();
    }

    /**
     * Returns the game with the given id.
     *
     * @param id
     * @return
     */
    public static Game getGameById(Long id) {
        return Game.find.byId(id);
    }
}
