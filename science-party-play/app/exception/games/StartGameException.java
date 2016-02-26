package exception.games;

import models.ebean.Game;

/**
 * This exception is thrown if a game starts to fail.
 */
public class StartGameException extends Exception {
    Game game = null;

    public StartGameException() {
    }

    public StartGameException(String message) {
        super(message);
    }

    public StartGameException(String message, Game game) {
        super(message);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
