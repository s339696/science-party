package exception.games;

import models.ebean.Game;

/**
 * Is throws when a Game is stopped.
 */
public class StopGameException extends GameException {

    public StopGameException() {
    }

    public StopGameException(String message) {
        super(message);
    }

    public StopGameException(String message, Game game) {
        super(message, game);
    }
}
