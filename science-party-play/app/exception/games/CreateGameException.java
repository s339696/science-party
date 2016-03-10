package exception.games;

import models.ebean.Game;

/**
 * Created by ewolf on 08.03.2016.
 */
public class CreateGameException extends GameException {
    public CreateGameException() {
    }

    public CreateGameException(String message) {
        super(message);
    }

    public CreateGameException(String message, Game game) {
        super(message, game);
    }
}
