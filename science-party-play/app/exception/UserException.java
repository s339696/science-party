package exception;

/**
 * Login exception is thrown when a registration fails because of some case
 */
public class UserException extends Exception {

    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }
}
