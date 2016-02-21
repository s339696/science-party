package exception;

/**
 * Login exception is thrown if a login fails
 */
public class LoginException extends Exception {

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }
}
