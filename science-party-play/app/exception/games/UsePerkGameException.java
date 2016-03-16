package exception.games;

import models.ebean.Game;

/**
 * Is thrown if a perk is used or failed to use.
 */
public class UsePerkGameException extends GameException {

    public UsePerkGameException() {
    }

    public UsePerkGameException(String message, Game game) {
        super(message, game);
    }

    public UsePerkGameException(String message) {
        super(message);
    }
}
