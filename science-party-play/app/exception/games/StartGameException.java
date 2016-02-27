package exception.games;

import models.ebean.Game;

/**
 * This exception is thrown if a game starts or fail to start.
 */
public class StartGameException extends GameException {

    public StartGameException() {
    }

    public StartGameException(String message) {
        super(message);
    }

    public StartGameException(String message, Game game) {
        super(message, game);
    }
}
