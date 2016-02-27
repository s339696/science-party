package exception.games;

import models.ebean.Game;

/**
 * Is thrown if a game can't be accepted.
 */
public class GameException extends Exception {

    Game game = null;

    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Game game) {
        super(message);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
