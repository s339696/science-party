package exception.games;

/**
 * Is thrown if a game can't be accepted.
 */
public class GameException extends Exception {

    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }
}
